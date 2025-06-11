package com.terning.server.kotlin.application.scrap

import com.terning.server.kotlin.application.scrap.dto.ScrapRequest
import com.terning.server.kotlin.application.scrap.dto.ScrapUpdateRequest
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncement
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncementRepository
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementDeadline
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipTitle
import com.terning.server.kotlin.domain.scrap.Scrap
import com.terning.server.kotlin.domain.scrap.ScrapRepository
import com.terning.server.kotlin.domain.scrap.exception.ScrapErrorCode
import com.terning.server.kotlin.domain.scrap.exception.ScrapException
import com.terning.server.kotlin.domain.scrap.vo.Color
import com.terning.server.kotlin.domain.user.User
import com.terning.server.kotlin.domain.user.UserRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Optional

class ScrapServiceTest {
    private val scrapRepository: ScrapRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)
    private val internshipAnnouncementRepository: InternshipAnnouncementRepository = mockk(relaxed = true)
    private lateinit var scrapService: ScrapService
    private lateinit var clock: Clock

    private val userId = 1L
    private val announcementId = 100L
    private val request = ScrapRequest(color = "BLUE")

    @BeforeEach
    fun setUp() {
        clock = Clock.fixed(Instant.parse("2025-06-08T00:00:00Z"), ZoneId.systemDefault())
        scrapService = ScrapService(scrapRepository, userRepository, internshipAnnouncementRepository, clock)
    }

    @Nested
    @DisplayName("스크랩 추가")
    inner class ScrapTest {
        @Test
        @DisplayName("이미 스크랩한 경우 예외가 발생한다")
        fun scrapFailsIfAlreadyScrapped() {
            // given
            givenScrapExists()

            // when & then
            val exception = assertThrows<ScrapException> { scrapService.scrap(userId, announcementId, request) }
            assertEquals(ScrapErrorCode.EXISTS_SCRAP_ALREADY, exception.errorCode)
        }

        @Test
        @DisplayName("공고를 찾을 수 없으면 예외가 발생한다")
        fun scrapFailsIfAnnouncementNotFound() {
            // given
            givenScrapDoesNotExist()
            every { internshipAnnouncementRepository.findById(announcementId) } returns Optional.empty()

            // when & then
            val exception = assertThrows<ScrapException> { scrapService.scrap(userId, announcementId, request) }
            assertEquals(ScrapErrorCode.INTERN_SHIP_ANNOUNCEMENT_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("사용자를 찾을 수 없으면 예외가 발생한다")
        fun scrapFailsIfUserNotFound() {
            // given
            givenScrapDoesNotExist()
            givenAnnouncementExists(mockk())
            every { userRepository.findById(userId) } returns Optional.empty()

            // when & then
            val exception = assertThrows<ScrapException> { scrapService.scrap(userId, announcementId, request) }
            assertEquals(ScrapErrorCode.USER_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("스크랩에 성공한다")
        fun scrapSucceeds() {
            // given
            val user = mockk<User>()
            val announcement = mockk<InternshipAnnouncement>(relaxed = true)
            val scrapSlot = slot<Scrap>()

            givenScrapDoesNotExist()
            givenUserAndAnnouncementExist(user, announcement)
            every { scrapRepository.save(capture(scrapSlot)) } returns mockk()

            // when
            scrapService.scrap(userId, announcementId, request)

            // then
            verify { announcement.increaseScrapCount() }
            assertEquals(Color.BLUE.toHexString(), scrapSlot.captured.hexColor())
        }
    }

    @Nested
    @DisplayName("스크랩 색상 변경")
    inner class UpdateScrapTest {
        private val updateRequest = ScrapUpdateRequest(color = "RED")
        private val scrap = mockk<Scrap>(relaxed = true)

        @Test
        @DisplayName("스크랩이 존재하지 않으면 예외가 발생한다")
        fun updateFailsIfScrapNotFound() {
            // given
            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns null

            // when & then
            val exception = assertThrows<ScrapException> { scrapService.updateScrap(userId, announcementId, updateRequest) }
            assertEquals(ScrapErrorCode.SCRAP_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("스크랩 색상을 성공적으로 업데이트한다")
        fun updateSucceeds() {
            // given
            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns scrap
            every { scrap.updateColor(Color.RED) } just runs
            every { scrapRepository.save(scrap) } returns scrap

            // when
            scrapService.updateScrap(userId, announcementId, updateRequest)

            // then
            verify { scrap.updateColor(Color.RED) }
            verify { scrapRepository.save(scrap) }
        }
    }

    @Nested
    @DisplayName("스크랩 취소")
    inner class CancelScrapTest {
        private val scrap = mockk<Scrap>()
        private val announcement = mockk<InternshipAnnouncement>(relaxed = true)

        @Test
        @DisplayName("스크랩이 존재하지 않으면 예외가 발생한다")
        fun cancelFailsIfScrapNotFound() {
            // given
            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns null

            // when & then
            val exception = assertThrows<ScrapException> { scrapService.cancelScrap(userId, announcementId) }
            assertEquals(ScrapErrorCode.SCRAP_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("공고가 존재하지 않으면 예외가 발생한다")
        fun cancelFailsIfAnnouncementNotFound() {
            // given
            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns scrap
            every { internshipAnnouncementRepository.findById(announcementId) } returns Optional.empty()

            // when & then
            val exception = assertThrows<ScrapException> { scrapService.cancelScrap(userId, announcementId) }
            assertEquals(ScrapErrorCode.INTERN_SHIP_ANNOUNCEMENT_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("스크랩 취소에 성공한다")
        fun cancelSucceeds() {
            // given
            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns scrap
            every { internshipAnnouncementRepository.findById(announcementId) } returns Optional.of(announcement)
            every { scrapRepository.delete(scrap) } just runs

            // when
            scrapService.cancelScrap(userId, announcementId)

            // then
            verify { announcement.decreaseScrapCount() }
            verify { scrapRepository.delete(scrap) }
        }
    }

    @Nested
    @DisplayName("월간 스크랩 마감일 조회")
    inner class MonthlyScrapDeadlineTest {
        @Test
        @DisplayName("마감일 기준으로 그룹핑된 스크랩을 반환한다")
        fun returnsGroupedScrapsByDeadline() {
            // given
            val deadline = LocalDate.of(2025, 6, 30)
            val scrapId = 1L

            val title = mockk<InternshipTitle>()
            every { title.value } returns "테스트 공고"

            val internshipAnnouncementDeadline = mockk<InternshipAnnouncementDeadline>()
            every { internshipAnnouncementDeadline.value } returns deadline

            val internshipAnnouncement = mockk<InternshipAnnouncement>()
            every { internshipAnnouncement.title } returns title
            every { internshipAnnouncement.internshipAnnouncementDeadline } returns internshipAnnouncementDeadline

            val scrap = mockk<Scrap>()
            every { scrap.id } returns scrapId
            every { scrap.internshipAnnouncement } returns internshipAnnouncement
            every { scrap.hexColor() } returns "#4AA9F2"

            every {
                scrapRepository.findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(
                    userId = userId,
                    start = LocalDate.of(2025, 6, 1),
                    end = LocalDate.of(2025, 6, 30),
                )
            } returns listOf(scrap)

            // when
            val response = scrapService.monthlyScrapDeadlines(userId, 2025, 6)

            // then
            assertEquals(1, response.monthlyScrapDeadline.size)
            val group = response.monthlyScrapDeadline.first()
            assertEquals("2025-06-30", group.deadline)
            assertEquals(1, group.scraps.size)
            assertEquals(scrapId, group.scraps.first().scrapId)
            assertEquals("테스트 공고", group.scraps.first().title)
            assertEquals("#4AA9F2", group.scraps.first().color)
        }

        @Test
        @DisplayName("스크랩 ID가 null이면 예외가 발생한다")
        fun throwsExceptionWhenScrapIdIsNull() {
            // given
            val announcement = mockk<InternshipAnnouncement>(relaxed = true)
            val scrap = mockk<Scrap>()

            every { scrap.id } returns null
            every { scrap.internshipAnnouncement } returns announcement
            every {
                scrapRepository.findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(any(), any(), any())
            } returns listOf(scrap)

            // when & then
            val exception =
                assertThrows<ScrapException> {
                    scrapService.monthlyScrapDeadlines(userId, 2025, 6)
                }
            assertEquals(ScrapErrorCode.SCRAP_ID_NULL, exception.errorCode)
        }
    }

    @Nested
    @DisplayName("상세 월간 스크랩 조회")
    inner class DetailedMonthlyScrapTest {
        @Test
        @DisplayName("마감일 기준으로 상세 스크랩 정보를 반환한다")
        fun returnsDetailedScrapsByDeadline() {
            // given
            val deadline = LocalDate.now(clock).plusDays(3)
            val workingPeriod = 6

            val announcement = mockk<InternshipAnnouncement>(relaxed = true)
            every { announcement.id } returns 1L
            every { announcement.company.logoUrl.value } returns "http://image.url/logo.png"
            every { announcement.title.value } returns "상세 공고"
            every { announcement.workingPeriod.toString() } returns "${workingPeriod}개월"
            every { announcement.internshipAnnouncementDeadline.value } returns deadline
            every { announcement.startDate.year.value } returns 2025
            every { announcement.startDate.month.value } returns 6

            val scrap = mockk<Scrap>()
            every { scrap.internshipAnnouncement } returns announcement
            every { scrap.hexColor() } returns "#123456"

            every {
                scrapRepository.findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(any(), any(), any())
            } returns listOf(scrap)

            // when
            val response = scrapService.detailedMonthlyScraps(userId, 2025, 6)

            // then
            val group = response.dailyGroups.first()
            val detailedScrap = group.scraps.first()

            assertEquals(1L, detailedScrap.announcementId)
            assertEquals("상세 공고", detailedScrap.title)
            assertEquals("${workingPeriod}개월", detailedScrap.workingPeriod)
            assertEquals(true, detailedScrap.isScrapped)
            assertEquals("#123456", detailedScrap.hexColor)
            assertEquals("http://image.url/logo.png", detailedScrap.companyImageUrl)
            assertEquals("2025년 6월 11일", detailedScrap.deadline)
            assertEquals("2025년 6월", detailedScrap.startYearMonth)
        }

        @Test
        @DisplayName("공고 ID가 null이면 예외가 발생한다")
        fun throwsExceptionWhenAnnouncementIdIsNull() {
            // given
            val announcement = mockk<InternshipAnnouncement>(relaxed = true)
            every { announcement.id } returns null
            val scrap = mockk<Scrap>()
            every { scrap.internshipAnnouncement } returns announcement
            every {
                scrapRepository.findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(any(), any(), any())
            } returns listOf(scrap)

            // when & then
            val exception =
                assertThrows<ScrapException> {
                    scrapService.detailedMonthlyScraps(userId, 2025, 6)
                }
            assertEquals(ScrapErrorCode.SCRAP_ID_NULL, exception.errorCode)
        }
    }

    @Nested
    @DisplayName("일간 스크랩 조회")
    inner class DailyScrapTest {
        @Test
        @DisplayName("지정한 날짜에 해당하는 스크랩 정보를 반환한다")
        fun returnsDailyScraps() {
            // given
            val date = LocalDate.of(2025, 6, 8)
            val announcement = mockk<InternshipAnnouncement>(relaxed = true)
            every { announcement.id } returns 1L
            every { announcement.company.logoUrl.value } returns "http://image.url/logo.png"
            every { announcement.title.value } returns "일간 공고"
            every { announcement.workingPeriod.toString() } returns "2개월"
            every { announcement.internshipAnnouncementDeadline.value } returns date
            every { announcement.startDate.year.value } returns 2025
            every { announcement.startDate.month.value } returns 6

            val scrap = mockk<Scrap>()
            every { scrap.internshipAnnouncement } returns announcement
            every { scrap.hexColor() } returns "#ABCDEF"
            every { scrapRepository.findScrapsByUserIdAndDeadlineOrderByDeadline(userId, date) } returns listOf(scrap)

            // when
            val result = scrapService.dailyScraps(userId, date)

            // then
            assertEquals(1, result.size)
            val item = result.first()

            assertEquals(1L, item.announcementId)
            assertEquals("일간 공고", item.title)
            assertEquals("2개월", item.workingPeriod)
            assertEquals("http://image.url/logo.png", item.companyImageUrl)
            assertEquals(true, item.isScrapped)
            assertEquals("#ABCDEF", item.hexColor)
            assertEquals("2025년 6월 8일", item.deadline)
            assertEquals("2025년 6월", item.startYearMonth)
            assertEquals("D-DAY", item.dDay)
        }

        @Test
        @DisplayName("공고 ID가 null이면 예외가 발생한다")
        fun throwsExceptionWhenAnnouncementIdIsNull() {
            // given
            val date = LocalDate.of(2025, 6, 8)
            val announcement = mockk<InternshipAnnouncement>(relaxed = true)
            every { announcement.id } returns null
            val scrap = mockk<Scrap>()
            every { scrap.internshipAnnouncement } returns announcement
            every { scrapRepository.findScrapsByUserIdAndDeadlineOrderByDeadline(userId, date) } returns listOf(scrap)

            // when & then
            val exception =
                assertThrows<ScrapException> {
                    scrapService.dailyScraps(userId, date)
                }
            assertEquals(ScrapErrorCode.SCRAP_ID_NULL, exception.errorCode)
        }
    }

    private fun givenScrapExists() {
        every { scrapRepository.existsByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns true
    }

    private fun givenScrapDoesNotExist() {
        every { scrapRepository.existsByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns false
    }

    private fun givenAnnouncementExists(announcement: InternshipAnnouncement) {
        every { internshipAnnouncementRepository.findById(announcementId) } returns Optional.of(announcement)
    }

    private fun givenUserAndAnnouncementExist(
        user: User,
        announcement: InternshipAnnouncement,
    ) {
        givenAnnouncementExists(announcement)
        every { userRepository.findById(userId) } returns Optional.of(user)
    }
}
