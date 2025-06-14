package com.terning.server.kotlin.ui.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.terning.server.kotlin.application.calendar.CalendarService
import com.terning.server.kotlin.application.calendar.dto.DailyScrapsResponse
import com.terning.server.kotlin.application.calendar.dto.DetailedMonthlyScrapsResponse
import com.terning.server.kotlin.application.calendar.dto.MonthlyViewResponse
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncement
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

@WebMvcTest(CalendarController::class)
@ActiveProfiles("test")
class CalendarControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var calendarService: CalendarService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("일간 스크랩 데이터를 조회한다")
    fun getDailyScraps() {
        // given
        val userId = 1L
        val date = LocalDate.of(2025, 6, 8)
        val clock = Clock.fixed(date.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())

        val announcementMock =
            mockk<InternshipAnnouncement>(relaxed = true) {
                every { id } returns 1L
                every { company.logoUrl.value } returns "https://test.image/logo.png"
                every { title.value } returns "일간 인턴 모집"
                every { workingPeriod.toString() } returns "2개월"
                every { internshipAnnouncementDeadline.value } returns date
                every { startDate.year.value } returns 2025
                every { startDate.month.value } returns 6
            }

        val dailyScrapResponse =
            DailyScrapsResponse.from(
                announcement = announcementMock,
                isScrapped = true,
                hexColor = "#ABCDEF",
                clock = clock,
            )

        every { calendarService.getDailyScraps(userId, date) } returns listOf(dailyScrapResponse)

        // when & then
        mockMvc.get("/api/v1/calendar/daily") {
            param("date", "2025-06-08")
        }.andExpect {
            status { isOk() }
            jsonPath("$.status") { value(200) }
            jsonPath("$.result[0].announcementId") { value(1) }
            jsonPath("$.result[0].title") { value("일간 인턴 모집") }
            jsonPath("$.result[0].dday") { value("D-DAY") }
        }
    }

    @Test
    @DisplayName("월간 스크랩 데이터를 리스트 형태로 조회한다")
    fun getDetailedMonthlyScraps() {
        // given
        val userId = 1L
        val year = 2025
        val month = 6
        val clock = Clock.fixed(LocalDate.of(2025, 6, 25).atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())

        val announcementMock =
            mockk<InternshipAnnouncement>(relaxed = true) {
                every { id } returns 1L
                every { company.logoUrl.value } returns "https://test.image/logo.png"
                every { title.value } returns "백엔드 인턴 모집"
                every { workingPeriod.toString() } returns "3개월"
                every { internshipAnnouncementDeadline.value } returns LocalDate.of(2025, 6, 30)
                every { startDate.year.value } returns 2025
                every { startDate.month.value } returns 7
            }

        val detailedMonthlyScrapResponse =
            DetailedMonthlyScrapsResponse.from(
                announcement = announcementMock,
                isScrapped = true,
                hexColor = "#123456",
                clock = clock,
            )

        every { calendarService.getDetailedMonthlyScraps(userId, year, month) } returns listOf(detailedMonthlyScrapResponse)

        // when & then
        mockMvc.get("/api/v1/calendar/monthly-list") {
            param("year", year.toString())
            param("month", month.toString())
        }.andExpect {
            status { isOk() }
            jsonPath("$.status") { value(200) }
            jsonPath("$.result[0].announcementId") { value(1) }
            jsonPath("$.result[0].title") { value("백엔드 인턴 모집") }
            jsonPath("$.result[0].dday") { value("D-5") }
        }
    }

    @Test
    @DisplayName("월간 스크랩 데이터를 기본 형태로 조회한다")
    fun getMonthlyScraps() {
        // given
        val userId = 1L
        val year = 2025
        val month = 6
        val response =
            MonthlyViewResponse(
                deadlines =
                    listOf(
                        MonthlyViewResponse.DeadlineGroup(
                            deadline = "2025-06-30",
                            scraps =
                                listOf(
                                    MonthlyViewResponse.ScrapSummary(
                                        scrapId = 1L,
                                        title = "테스트 공고",
                                        color = "#4AA9F2",
                                    ),
                                ),
                        ),
                    ),
            )

        every { calendarService.getMonthlyScraps(userId, year, month) } returns response

        // when & then
        mockMvc.get("/api/v1/calendar/monthly-default") {
            param("year", year.toString())
            param("month", month.toString())
        }.andExpect {
            status { isOk() }
            jsonPath("$.status") { value(200) }
            jsonPath("$.result.deadlines[0].deadline") { value("2025-06-30") }
            jsonPath("$.result.deadlines[0].scraps[0].title") { value("테스트 공고") }
        }
    }
}
