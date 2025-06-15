package com.terning.server.kotlin.application.home

import com.querydsl.core.Tuple
import com.terning.server.kotlin.domain.filter.Filter
import com.terning.server.kotlin.domain.filter.FilterRepository
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncement
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncementRepository
import com.terning.server.kotlin.domain.internshipAnnouncement.QInternshipAnnouncement.internshipAnnouncement
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.Company
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.CompanyCategory
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.CompanyLogoUrl
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.CompanyName
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipTitle
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipWorkingPeriod
import com.terning.server.kotlin.domain.scrap.QScrap.scrap
import com.terning.server.kotlin.domain.scrap.Scrap
import com.terning.server.kotlin.domain.scrap.ScrapRepository
import com.terning.server.kotlin.domain.user.User
import com.terning.server.kotlin.domain.user.UserRepository
import com.terning.server.kotlin.domain.user.exception.UserErrorCode
import com.terning.server.kotlin.domain.user.exception.UserException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Optional

class HomeServiceTest {
    private lateinit var internshipRepository: InternshipAnnouncementRepository
    private lateinit var userRepository: UserRepository
    private lateinit var filterRepository: FilterRepository
    private lateinit var scrapRepository: ScrapRepository
    private lateinit var clock: Clock
    private lateinit var service: HomeService

    private val userId = 1L
    private lateinit var user: User
    private val pageable = PageRequest.of(0, 10)
    private val sortBy = "recent"

    @BeforeEach
    fun setUp() {
        internshipRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        filterRepository = mockk(relaxed = true)
        scrapRepository = mockk(relaxed = true)
        user = mockk()
        val fixedInstant = Instant.parse("2025-06-08T00:00:00Z")
        clock = Clock.fixed(fixedInstant, ZoneId.systemDefault())
        service = HomeService(internshipRepository, userRepository, filterRepository, scrapRepository, clock)
    }

    @Nested
    @DisplayName("getFilteredAnnouncements 메소드는")
    inner class GetFilteredAnnouncements {
        @Test
        @DisplayName("사용자를 찾지 못하면 예외를 던진다")
        fun `it throws exception when user not found`() {
            // given
            every { userRepository.findById(userId) } returns Optional.empty()

            // when & then
            val exception =
                assertThrows<InternshipAnnouncementException> {
                    service.getFilteredAnnouncements(userId, sortBy, pageable)
                }
            assertEquals(
                InternshipAnnouncementErrorCode.NOT_FOUND_USER_EXCEPTION,
                exception.errorCode,
            )
        }

        @Test
        @DisplayName("기본 필터일 경우 필터링 없이 전체 공고를 조회한다")
        fun `it returns all announcements when filter is default`() {
            // given
            val filter = mockk<Filter> { every { isDefault() } returns true }
            val internship = createMockInternship(id = 100L, title = "[테스트] 백엔드 인턴 모집")
            val tuple = createMockTuple(internship)

            every { userRepository.findById(userId) } returns Optional.of(user)
            every { filterRepository.findLatestByUser(user) } returns filter
            every {
                internshipRepository.findAllInternshipsWithScrapInfo(user, sortBy, pageable)
            } returns PageImpl(listOf(tuple))

            // when
            val response = service.getFilteredAnnouncements(userId, sortBy, pageable)

            // then
            assertEquals(1, response.totalCount)
            assertEquals("[테스트] 백엔드 인턴 모집", response.announcements.first().title)
        }
    }

    @Nested
    @DisplayName("홈 화면 - 곧 마감되는 스크랩 조회")
    inner class FindUpcomingDeadlineScrapsTest {
        @Test
        @DisplayName("사용자가 존재하지 않으면 UserException이 발생한다")
        fun throwsUserExceptionWhenUserNotFound() {
            // given
            every { userRepository.existsById(userId) } returns false

            // when & then
            val exception =
                assertThrows<UserException> {
                    service.findUpcomingDeadlineScraps(userId)
                }
            assertEquals(UserErrorCode.USER_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("스크랩한 공고가 없으면 hasScrapped가 false인 응답을 반환한다")
        fun returnsHasScrappedFalseWhenNoScrapsExist() {
            // given
            every { userRepository.existsById(userId) } returns true
            every { scrapRepository.existsByUserId(userId) } returns false

            // when
            val response = service.findUpcomingDeadlineScraps(userId)

            // then
            assertEquals(false, response.hasScrapped)
            assertEquals("아직 스크랩된 인턴 공고가 없어요!", response.message)
            assertEquals(0, response.scraps.size)
        }

        @Test
        @DisplayName("스크랩은 했지만 7일 내 마감 공고가 없으면 hasScrapped가 true이고 빈 리스트를 반환한다")
        fun returnsEmptyListWhenUpcomingScrapsDoNotExist() {
            // given
            every { userRepository.existsById(userId) } returns true
            every { scrapRepository.existsByUserId(userId) } returns true
            every {
                scrapRepository.findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(
                    userId = userId,
                    start = LocalDate.now(clock),
                    end = LocalDate.now(clock).plusDays(7),
                )
            } returns emptyList()

            // when
            val response = service.findUpcomingDeadlineScraps(userId)

            // then
            assertEquals(true, response.hasScrapped)
            assertEquals("일주일 내에 마감인 공고가 없어요\n캘린더에서 스크랩한 공고 일정을 확인해 보세요", response.message)
            assertEquals(0, response.scraps.size)
        }

        @Test
        @DisplayName("마감 임박 스크랩 공고를 성공적으로 조회한다")
        fun returnsUpcomingDeadlineScrapsSuccessfully() {
            // given
            val announcement = mockk<InternshipAnnouncement>(relaxed = true)
            val scrap = mockk<Scrap>(relaxed = true)
            val startDate = LocalDate.now(clock)
            val endDate = startDate.plusDays(7)

            every { announcement.id } returns 1L
            every { announcement.company.logoUrl.value } returns "http://logo.url/logo.png"
            every { announcement.company.name.value } returns "터닝 기업"
            every { announcement.title.value } returns "마감 임박 공고"
            every { announcement.workingPeriod.toString() } returns "3개월"
            every { announcement.internshipAnnouncementDeadline.value } returns LocalDate.now(clock).plusDays(5)
            every { announcement.startDate.year.value } returns 2025
            every { announcement.startDate.month.value } returns 7
            every { scrap.internshipAnnouncement } returns announcement
            every { scrap.hexColor() } returns "#FF5733"
            every { userRepository.existsById(userId) } returns true
            every { scrapRepository.existsByUserId(userId) } returns true
            every {
                scrapRepository.findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(userId, startDate, endDate)
            } returns listOf(scrap)

            // when
            val response = service.findUpcomingDeadlineScraps(userId)

            // then
            assertEquals(true, response.hasScrapped)
            assertEquals("곧 마감되는 스크랩 공고를 성공적으로 조회했습니다.", response.message)
            assertEquals(1, response.scraps.size)

            val detail = response.scraps.first()
            assertEquals(1L, detail.internshipAnnouncementId)
            assertEquals("터닝 기업", detail.companyInfo)
            assertEquals("마감 임박 공고", detail.title)
            assertEquals("D-5", detail.dDay)
            assertEquals(true, detail.isScrapped)
            assertEquals("#FF5733", detail.color)
        }
    }

    private fun createMockInternship(
        id: Long,
        title: String,
    ): InternshipAnnouncement {
        return mockk {
            every { this@mockk.id } returns id
            every { this@mockk.title } returns InternshipTitle.from(title)
            every { this@mockk.workingPeriod } returns InternshipWorkingPeriod.from(3)
            every { this@mockk.company } returns
                Company.of(
                    name = CompanyName.from("회사"),
                    category = CompanyCategory.from("기타"),
                    logoUrl = CompanyLogoUrl.from("https://logo.com"),
                )
        }
    }

    private fun createMockTuple(internship: InternshipAnnouncement): Tuple {
        return mockk {
            every { get(internshipAnnouncement) } returns internship
            every { get(scrap.id) } returns null
            every { get(scrap.color) } returns null
        }
    }
}
