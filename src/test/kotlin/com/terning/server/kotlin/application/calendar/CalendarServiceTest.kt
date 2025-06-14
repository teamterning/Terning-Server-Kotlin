package com.terning.server.kotlin.application.calendar

import com.terning.server.kotlin.application.calendar.dto.DailyScrapsResponse
import com.terning.server.kotlin.application.calendar.dto.DetailedMonthlyScrapsResponse
import com.terning.server.kotlin.application.calendar.dto.MonthlyViewResponse
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncement
import com.terning.server.kotlin.domain.scrap.Scrap
import com.terning.server.kotlin.domain.scrap.ScrapRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class CalendarServiceTest {
    private lateinit var scrapRepository: ScrapRepository
    private lateinit var clock: Clock
    private lateinit var calendarService: CalendarService

    private val userId = 1L

    @BeforeEach
    fun setUp() {
        scrapRepository = mockk(relaxed = true)
        clock = Clock.fixed(Instant.parse("2025-06-08T00:00:00Z"), ZoneId.systemDefault())
        calendarService = CalendarService(scrapRepository, clock)
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
            val scrap = mockk<Scrap>(relaxed = true)

            every { announcement.id } returns 1L
            every { announcement.title.value } returns "일간 공고"
            every { announcement.internshipAnnouncementDeadline.value } returns date
            every { scrap.internshipAnnouncement } returns announcement
            every { scrap.hexColor() } returns "#ABCDEF"
            every { scrapRepository.findScrapsByUserIdAndDeadlineOrderByDeadline(userId, date) } returns listOf(scrap)

            // when
            val result = calendarService.getDailyScraps(userId, date)

            // then
            assertEquals(1, result.size)
            val item = result.first()
            assertTrue(item is DailyScrapsResponse)
            assertEquals(1L, item.announcementId)
            assertEquals("일간 공고", item.title)
            assertEquals("D-DAY", item.dDay)
        }
    }

    @Nested
    @DisplayName("월간 스크랩 조회 (기본 뷰)")
    inner class MonthlyScrapTest {
        @Test
        @DisplayName("월별 스크랩 정보를 마감일 기준으로 그룹핑하여 반환한다")
        fun returnsMonthlyScraps() {
            // given
            val year = 2025
            val month = 6
            val startDate = LocalDate.of(year, month, 1)
            val endDate = startDate.plusMonths(1).minusDays(1)
            val announcement = mockk<InternshipAnnouncement>(relaxed = true)
            val scrap = mockk<Scrap>(relaxed = true)

            every { announcement.title.value } returns "월간 공고"
            every { announcement.internshipAnnouncementDeadline.value } returns LocalDate.of(2025, 6, 20)
            every { scrap.id } returns 1L
            every { scrap.internshipAnnouncement } returns announcement
            every { scrap.hexColor() } returns "#4AA9F2"
            every { scrapRepository.findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(userId, startDate, endDate) } returns listOf(scrap)

            // when
            val result = calendarService.getMonthlyScraps(userId, year, month)

            // then
            assertTrue(result is MonthlyViewResponse)
            assertEquals(1, result.deadlines.size)
            val group = result.deadlines.first()
            assertEquals("2025-06-20", group.deadline)
            assertEquals("월간 공고", group.scraps.first().title)
        }
    }

    @Nested
    @DisplayName("월간 스크랩 조회 (리스트 뷰)")
    inner class DetailedMonthlyScrapTest {
        @Test
        @DisplayName("월별 스크랩 상세 정보를 리스트로 반환한다")
        fun returnsDetailedMonthlyScraps() {
            // given
            val year = 2025
            val month = 6
            val startDate = LocalDate.of(year, month, 1)
            val endDate = startDate.plusMonths(1).minusDays(1)
            val announcement = mockk<InternshipAnnouncement>(relaxed = true)
            val scrap = mockk<Scrap>(relaxed = true)

            every { announcement.id } returns 2L
            every { announcement.title.value } returns "월간 상세 공고"
            every { announcement.internshipAnnouncementDeadline.value } returns LocalDate.of(2025, 6, 25)
            every { scrap.internshipAnnouncement } returns announcement
            every { scrapRepository.findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(userId, startDate, endDate) } returns listOf(scrap)

            // when
            val result = calendarService.getDetailedMonthlyScraps(userId, year, month)

            // then
            assertEquals(1, result.size)
            val item = result.first()
            assertTrue(item is DetailedMonthlyScrapsResponse)
            assertEquals(2L, item.announcementId)
            assertEquals("월간 상세 공고", item.title)
            assertEquals("D-17", item.dDay)
        }
    }
}
