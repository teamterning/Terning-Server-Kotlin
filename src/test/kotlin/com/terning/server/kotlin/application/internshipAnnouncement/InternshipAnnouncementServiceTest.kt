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
import java.util.Optional

class InternshipAnnouncementServiceTest {
    private lateinit var internshipRepository: InternshipAnnouncementRepository
    private lateinit var userRepository: UserRepository
    private lateinit var filterRepository: FilterRepository
    private lateinit var service: InternshipAnnouncementService

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
        service = InternshipAnnouncementService(internshipRepository, userRepository, filterRepository)
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
}
