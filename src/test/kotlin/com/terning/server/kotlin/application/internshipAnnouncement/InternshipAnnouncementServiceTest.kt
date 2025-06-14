package com.terning.server.kotlin.application.internshipAnnouncement

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
import com.terning.server.kotlin.domain.scrap.exception.ScrapErrorCode
import com.terning.server.kotlin.domain.scrap.exception.ScrapException
import com.terning.server.kotlin.domain.user.User
import com.terning.server.kotlin.domain.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
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

class InternshipAnnouncementServiceTest {
    private lateinit var internshipRepository: InternshipAnnouncementRepository
    private lateinit var userRepository: UserRepository
    private lateinit var filterRepository: FilterRepository
    private lateinit var service: InternshipAnnouncementService
    private lateinit var scrapRepository: ScrapRepository
    private lateinit var clock: Clock

    private val userId = 1L
    private lateinit var user: User
    private val pageable = PageRequest.of(0, 10)
    private val sortBy = "recent"

    @BeforeEach
    fun setUp() {
        internshipRepository = mockk()
        userRepository = mockk()
        filterRepository = mockk()
        user = mockk()
        scrapRepository = mockk()
        clock = Clock.fixed(Instant.parse("2025-06-08T00:00:00Z"), ZoneId.systemDefault())
        service =
            InternshipAnnouncementService(
                internshipRepository = internshipRepository,
                userRepository = userRepository,
                filterRepository = filterRepository,
                scrapRepository = scrapRepository,
                clock = clock,
            )
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
            Assertions.assertEquals(
                InternshipAnnouncementErrorCode.NOT_FOUND_USER_EXCEPTION,
                exception.errorCode,
            )
        }

        @Test
        @DisplayName("필터와 공고가 모두 없으면 빈 응답을 반환한다")
        fun `it returns empty response when no filter and no announcements`() {
            // given
            every { userRepository.findById(userId) } returns Optional.of(user)
            every { filterRepository.findLatestByUser(user) } returns null
            every {
                internshipRepository.findAllInternshipsWithScrapInfo(user, sortBy, pageable)
            } returns PageImpl(emptyList())

            // when
            val response = service.getFilteredAnnouncements(userId, sortBy, pageable)

            // then
            Assertions.assertEquals(0, response.totalPages)
            Assertions.assertEquals(0, response.totalCount)
            Assertions.assertFalse(response.hasNext)
            Assertions.assertTrue(response.announcements.isEmpty())
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
            // [수정] 가독성 및 라인 길이 준수를 위해 줄바꿈 적용
            every {
                internshipRepository.findAllInternshipsWithScrapInfo(user, sortBy, pageable)
            } returns PageImpl(listOf(tuple))

            // when
            val response = service.getFilteredAnnouncements(userId, sortBy, pageable)

            // then
            Assertions.assertEquals(1, response.totalCount)
            Assertions.assertEquals("[테스트] 백엔드 인턴 모집", response.announcements.first().title)
        }

        @Test
        @DisplayName("지정된 필터가 존재하면 필터링된 공고를 조회한다")
        fun `it returns filtered announcements when filter exists`() {
            // given
            val filter = mockk<Filter> { every { isDefault() } returns false }
            val internship = createMockInternship(id = 200L, title = "프론트엔드 인턴 모집")
            val tuple = createMockTuple(internship)

            every { userRepository.findById(userId) } returns Optional.of(user)
            every { filterRepository.findLatestByUser(user) } returns filter
            every {
                internshipRepository.findFilteredInternshipsWithScrapInfo(
                    user,
                    filter,
                    sortBy,
                    pageable,
                )
            } returns PageImpl(listOf(tuple))

            // when
            val response = service.getFilteredAnnouncements(userId, sortBy, pageable)

            // then
            Assertions.assertEquals(1, response.totalCount)
            Assertions.assertEquals("프론트엔드 인턴 모집", response.announcements.first().title)
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

    @Nested
    @DisplayName("getDetailAnnouncement 메소드는")
    inner class GetDetailAnnouncement {
        @Test
        @DisplayName("공고를 찾을 수 없으면 예외를 던진다")
        fun `it throws exception when internship not found`() {
            // given
            val internshipId = 1L
            every { internshipRepository.findById(internshipId) } returns Optional.empty()

            // when & then
            val exception =
                assertThrows<ScrapException> {
                    service.getDetailAnnouncement(userId = 1L, internshipAnnouncementId = internshipId)
                }
            Assertions.assertEquals(ScrapErrorCode.INTERN_SHIP_ANNOUNCEMENT_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("스크랩 정보가 없으면 isScrapped=false, color=null을 반환한다")
        fun `it returns non-scrapped info if no scrap found`() {
            // given
            val internship =
                mockk<InternshipAnnouncement> {
                    every { company.logoUrl.value } returns "https://media-cdn.linkareer.com/activity_manager/logos/467093"
                    every { internshipAnnouncementDeadline.value } returns LocalDate.of(2025, 7, 1)
                    every { title.value } returns "[카카오페이] 가맹점 업무 지원 어시스턴트 채용"
                    every { workingPeriod.value } returns 3
                    every { startDate.year.value } returns 2025
                    every { startDate.month.value } returns 8
                    every { internshipAnnouncementScrapCount.value } returns 0
                    every { internshipAnnouncementViewCount.value } returns 1
                    every { company.name.value } returns "카카오페이"
                    every { company.category.displayName } returns "대기엽/중견기업"
                    every { qualifications } returns "졸업 예정자, 휴학생 가능"
                    every { filterJobType.type } returns "IT"
                    every { detail } returns "[지원자격] 성실하고 꼼꼼한 성격을 소유하신 분이면 좋겠어요."
                    every { url.value } returns "https://kakaopay.career.greetinghr.com/ko/o/136662"
                    every { increaseViewCount() } returns Unit
                }

            every { internshipRepository.findById(any()) } returns Optional.of(internship)
            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(any(), any()) } returns null

            // when
            val result = service.getDetailAnnouncement(userId = 1L, internshipAnnouncementId = 1L)

            // then
            Assertions.assertFalse(result.isScrapped)
            Assertions.assertNull(result.color)
        }

        @Test
        @DisplayName("스크랩 정보가 있으면 isScrapped=true, color이 함께 반환한다")
        fun `it returns scrapped info if scrap found`() {
            // given
            val internship =
                mockk<InternshipAnnouncement> {
                    every { company.logoUrl.value } returns "https://media-cdn.linkareer.com/activity_manager/logos/467093"
                    every { internshipAnnouncementDeadline.value } returns LocalDate.of(2025, 7, 1)
                    every { title.value } returns "[카카오페이] 가맹점 업무 지원 어시스턴트 채용"
                    every { workingPeriod.value } returns 6
                    every { startDate.year.value } returns 2025
                    every { startDate.month.value } returns 8
                    every { internshipAnnouncementScrapCount.value } returns 2
                    every { internshipAnnouncementViewCount.value } returns 5
                    every { company.name.value } returns "카카오페이"
                    every { company.category.displayName } returns "대기엽/중견기업"
                    every { qualifications } returns "졸업 예정자, 휴학생 가능"
                    every { filterJobType.type } returns "IT"
                    every { detail } returns "[지원자격] 성실하고 꼼꼼한 성격을 소유하신 분이면 좋겠어요."
                    every { url.value } returns "https://kakaopay.career.greetinghr.com/ko/o/136662"
                    every { increaseViewCount() } returns Unit
                }

            val scrap =
                mockk<Scrap> {
                    every { hexColor() } returns "#F3A649"
                }

            every { internshipRepository.findById(any()) } returns Optional.of(internship)
            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(any(), any()) } returns scrap

            // when
            val result = service.getDetailAnnouncement(userId = 1L, internshipAnnouncementId = 1L)

            // then
            Assertions.assertTrue(result.isScrapped)
            Assertions.assertEquals("#F3A649", result.color)
        }
    }
}
