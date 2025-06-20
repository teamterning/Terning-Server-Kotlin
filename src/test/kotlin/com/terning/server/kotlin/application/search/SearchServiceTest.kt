package com.terning.server.kotlin.application.search

import com.querydsl.core.Tuple
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncement
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncementRepository
import com.terning.server.kotlin.domain.internshipAnnouncement.QInternshipAnnouncement.internshipAnnouncement
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.CompanyLogoUrl
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementDeadline
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipTitle
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipWorkingPeriod
import com.terning.server.kotlin.domain.scrap.QScrap.scrap
import com.terning.server.kotlin.domain.scrap.vo.Color
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

class SearchServiceTest {
    private lateinit var internshipAnnouncementRepository: InternshipAnnouncementRepository
    private lateinit var userRepository: UserRepository
    private lateinit var clock: Clock
    private lateinit var service: SearchService

    private val userId = 1L
    private lateinit var user: User
    private val pageable = PageRequest.of(0, 10)
    private lateinit var now: LocalDate

    @BeforeEach
    fun setUp() {
        internshipAnnouncementRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        val fixedInstant = Instant.parse("2025-06-15T00:00:00Z")
        clock = Clock.fixed(fixedInstant, ZoneId.of("Asia/Seoul"))
        service = SearchService(internshipAnnouncementRepository, userRepository, clock)
        user = mockk()
        now = LocalDate.now(clock)
    }

    @Nested
    @DisplayName("search 메소드는")
    inner class Search {
        @Test
        @DisplayName("사용자를 찾을 수 없으면 UserException을 던진다")
        fun `it throws exception when user not found`() {
            // given
            every { userRepository.findById(userId) } returns Optional.empty()

            // when & then
            val exception =
                assertThrows<UserException> {
                    service.search(userId, "키워드", "recent", pageable)
                }
            assertEquals(UserErrorCode.USER_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 페이지를 반환한다")
        fun `it returns empty page when no results found`() {
            // given
            every { userRepository.findById(userId) } returns Optional.of(user)
            every {
                internshipAnnouncementRepository.findByKeywordWithScrapInfo(
                    user,
                    "없는 키워드",
                    "recent",
                    pageable,
                    now,
                )
            } returns PageImpl(emptyList())

            // when
            val response = service.search(userId, "없는 키워드", "recent", pageable)

            // then
            assertEquals(0, response.totalCount)
            assertEquals(0, response.announcements.size)
        }

        @Test
        @DisplayName("검색에 성공하면 DTO로 변환된 페이지를 반환한다")
        fun `it returns mapped dto page on success`() {
            // given
            val keyword = "네이버"
            val sortBy = "DEADLINE_SOON"
            val deadline = now.plusDays(10)

            val announcement = createMockInternship(1L, "네이버웹툰 BE 개발 인턴", deadline)
            val tuple = createMockTuple(announcement, 2L, Color.PURPLE)

            every { userRepository.findById(userId) } returns Optional.of(user)
            every {
                internshipAnnouncementRepository.findByKeywordWithScrapInfo(
                    user,
                    keyword,
                    sortBy,
                    pageable,
                    now,
                )
            } returns PageImpl(listOf(tuple))

            // when
            val response = service.search(userId, keyword, sortBy, pageable)

            // then
            assertEquals(1, response.totalCount)
            assertEquals(1, response.announcements.size)

            val resultDto = response.announcements.first()
            assertEquals(1L, resultDto.internshipAnnouncementId)
            assertEquals("네이버웹툰 BE 개발 인턴", resultDto.title)
            assertEquals(true, resultDto.isScrapped)
            assertEquals(Color.PURPLE.toHexString(), resultDto.color)
            assertEquals("D-10", resultDto.dDay)
        }
    }

    @Nested
    @DisplayName("getMostViewedAnnouncements 메소드는")
    inner class GetMostViewedAnnouncements {
        @Test
        @DisplayName("사용자를 찾을 수 없으면 UserException을 던진다")
        fun `it throws exception when user not found`() {
            // given
            every { userRepository.existsById(userId) } returns false

            // when & then
            val exception =
                assertThrows<UserException> {
                    service.getMostViewedAnnouncements(userId)
                }
            assertEquals(UserErrorCode.USER_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("조회수 많은 공고가 없으면 빈 리스트를 반환한다")
        fun `it returns empty list when no announcements found`() {
            // given
            every { userRepository.existsById(userId) } returns true
            every { internshipAnnouncementRepository.findTop5ByViews(now) } returns emptyList()

            // when
            val response = service.getMostViewedAnnouncements(userId)

            // then
            assertEquals(0, response.announcements.size)
        }

        @Test
        @DisplayName("조회수 많은 공고 조회에 성공하면 DTO 리스트를 반환한다")
        fun `it returns dto list on success`() {
            // given
            every { userRepository.existsById(userId) } returns true

            val announcement1 = createMockInternship(1L, "인기 공고 1", now)
            val announcement2 = createMockInternship(2L, "인기 공고 2", now)
            val mockAnnouncements = listOf(announcement1, announcement2)

            every { internshipAnnouncementRepository.findTop5ByViews(now) } returns mockAnnouncements

            // when
            val response = service.getMostViewedAnnouncements(userId)

            // then
            assertEquals(2, response.announcements.size)
            assertEquals(1L, response.announcements[0].internshipAnnouncementId)
            assertEquals("인기 공고 1", response.announcements[0].title)
            assertEquals(2L, response.announcements[1].internshipAnnouncementId)
            assertEquals("인기 공고 2", response.announcements[1].title)
        }
    }

    private fun createMockInternship(
        id: Long,
        title: String,
        deadline: LocalDate,
    ): InternshipAnnouncement {
        return mockk {
            every { this@mockk.id } returns id
            every { this@mockk.title } returns InternshipTitle.from(title)
            every { this@mockk.internshipAnnouncementDeadline } returns InternshipAnnouncementDeadline.from(deadline)
            every { this@mockk.workingPeriod } returns InternshipWorkingPeriod.from(3)
            every { this@mockk.company } returns
                mockk {
                    every { logoUrl } returns CompanyLogoUrl.from("http://logo.url/logo.png")
                }
            every { this@mockk.startDate } returns
                mockk {
                    every { year.value } returns 2025
                    every { month.value } returns 7
                }
        }
    }

    private fun createMockTuple(
        internship: InternshipAnnouncement,
        scrapId: Long?,
        scrapColor: Color?,
    ): Tuple {
        return mockk {
            every { get(internshipAnnouncement) } returns internship
            every { get(scrap.id) } returns scrapId
            every { get(scrap.color) } returns scrapColor
        }
    }
}
