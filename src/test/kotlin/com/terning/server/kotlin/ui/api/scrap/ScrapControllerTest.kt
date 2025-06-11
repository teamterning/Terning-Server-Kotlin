package com.terning.server.kotlin.ui.api.scrap

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.terning.server.kotlin.application.scrap.ScrapService
import com.terning.server.kotlin.application.scrap.dto.DetailedMonthlyScrapResponse
import com.terning.server.kotlin.application.scrap.dto.DetailedScrap
import com.terning.server.kotlin.application.scrap.dto.DetailedScrapGroup
import com.terning.server.kotlin.application.scrap.dto.MonthlyScrapDeadlineGroup
import com.terning.server.kotlin.application.scrap.dto.MonthlyScrapDeadlineResponse
import com.terning.server.kotlin.application.scrap.dto.MonthlyScrapDeadlineSummary
import com.terning.server.kotlin.application.scrap.dto.ScrapRequest
import com.terning.server.kotlin.application.scrap.dto.ScrapUpdateRequest
import com.terning.server.kotlin.ui.api.ScrapController
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

@WebMvcTest(ScrapController::class)
@ActiveProfiles("test")
class ScrapControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var scrapService: ScrapService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var scrapRequest: ScrapRequest
    private lateinit var scrapUpdateRequest: ScrapUpdateRequest

    @BeforeEach
    fun setUp() {
        scrapRequest = ScrapRequest(color = "BLUE")
        scrapUpdateRequest = ScrapUpdateRequest(color = "RED")
    }

    @Test
    @DisplayName("일간 스크랩 데이터를 조회한다")
    fun getDailyScraps() {
        val userId = 1L
        val date = LocalDate.of(2025, 6, 8)
        val clock =
            Clock.fixed(
                date.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault(),
            )

        val scrap =
            DetailedScrap.from(
                announcementId = 1L,
                companyImageUrl = "https://test.image/logo.png",
                title = "일간 인턴 모집",
                workingPeriod = "2개월",
                isScrapped = true,
                hexColor = "#ABCDEF",
                deadline = date,
                startYear = 2025,
                startMonth = 6,
                clock = clock,
            )

        every { scrapService.dailyScraps(userId, date) } returns listOf(scrap)

        mockMvc.get("/api/v1/calendar/daily") {
            param("date", "2025-06-08")
        }.andExpect {
            status { isOk() }
            jsonPath("$.status") { value(200) }
            jsonPath("$.result[0].announcementId") { value(1) }
            jsonPath("$.result[0].companyImageUrl") { value("https://test.image/logo.png") }
            jsonPath("$.result[0].title") { value("일간 인턴 모집") }
            jsonPath("$.result[0].workingPeriod") { value("2개월") }
            jsonPath("$.result[0].isScrapped") { value(true) }
            jsonPath("$.result[0].hexColor") { value("#ABCDEF") }
            jsonPath("$.result[0].startYearMonth") { value("2025년 6월") }
            // ✨ 수정: 'deadlineText' -> 'deadline'
            jsonPath("$.result[0].deadline") { value("2025년 6월 8일") }
            // ✨ 수정: 'formattedDeadline' -> 'dday'
            jsonPath("$.result[0].dday") { value("D-DAY") }
        }
    }

    @Test
    @DisplayName("월간 스크랩 데이터를 리스트 형태로 조회한다")
    fun getDetailedMonthlyScraps() {
        val userId = 1L
        val year = 2025
        val month = 6
        val clock =
            Clock.fixed(
                LocalDate.of(2025, 6, 25).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault(),
            )

        val detailedScrap =
            DetailedScrap.from(
                announcementId = 1L,
                companyImageUrl = "https://test.image/logo.png",
                title = "백엔드 인턴 모집",
                workingPeriod = "3개월",
                isScrapped = true,
                hexColor = "#123456",
                deadline = LocalDate.of(2025, 6, 30),
                startYear = 2025,
                startMonth = 7,
                clock = clock,
            )

        val detailedResponse =
            DetailedMonthlyScrapResponse(
                dailyGroups =
                    listOf(
                        DetailedScrapGroup(
                            deadline = "2025-06-30",
                            scraps = listOf(detailedScrap),
                        ),
                    ),
            )

        every { scrapService.detailedMonthlyScraps(userId, year, month) } returns detailedResponse

        mockMvc.get("/api/v1/calendar/monthly-list") {
            param("year", year.toString())
            param("month", month.toString())
        }.andExpect {
            status { isOk() }
            jsonPath("$.status") { value(200) }
            jsonPath("$.result.dailyGroups[0].deadline") { value("2025-06-30") }
            // ✨ 수정: 'internshipAnnouncementId' -> 'announcementId'
            jsonPath("$.result.dailyGroups[0].scraps[0].announcementId") { value(1) }
            // ✨ 수정: 'companyImage' -> 'companyImageUrl'
            jsonPath("$.result.dailyGroups[0].scraps[0].companyImageUrl") { value("https://test.image/logo.png") }
            jsonPath("$.result.dailyGroups[0].scraps[0].title") { value("백엔드 인턴 모집") }
            jsonPath("$.result.dailyGroups[0].scraps[0].workingPeriod") { value("3개월") }
            jsonPath("$.result.dailyGroups[0].scraps[0].isScrapped") { value(true) }
            // ✨ 수정: 'color' -> 'hexColor'
            jsonPath("$.result.dailyGroups[0].scraps[0].hexColor") { value("#123456") }
            jsonPath("$.result.dailyGroups[0].scraps[0].deadline") { value("2025년 6월 30일") }
            jsonPath("$.result.dailyGroups[0].scraps[0].startYearMonth") { value("2025년 7월") }
            jsonPath("$.result.dailyGroups[0].scraps[0].dday") { value("D-5") }
        }
    }

    @Test
    @DisplayName("월간 스크랩 데이터를 조회한다")
    fun getMonthlyScraps() {
        val userId = 1L
        val year = 2025
        val month = 6

        val summary =
            MonthlyScrapDeadlineSummary(
                scrapId = 1L,
                title = "테스트 공고",
                color = "#4AA9F2",
            )
        val group =
            MonthlyScrapDeadlineGroup(
                deadline = "2025-06-30",
                scraps = listOf(summary),
            )
        val response =
            MonthlyScrapDeadlineResponse(
                monthlyScrapDeadline = listOf(group),
            )

        every { scrapService.monthlyScrapDeadlines(userId, year, month) } returns response

        mockMvc.get("/api/v1/calendar/monthly-default") {
            param("year", year.toString())
            param("month", month.toString())
        }.andExpect {
            status { isOk() }
            jsonPath("$.status") { value(200) }
            jsonPath("$.result.monthlyScrapDeadline[0].deadline") { value("2025-06-30") }
            jsonPath("$.result.monthlyScrapDeadline[0].scraps[0].scrapId") { value(1) }
            jsonPath("$.result.monthlyScrapDeadline[0].scraps[0].title") { value("테스트 공고") }
            jsonPath("$.result.monthlyScrapDeadline[0].scraps[0].color") { value("#4AA9F2") }
        }
    }

    @Test
    @DisplayName("스크랩을 생성한다")
    fun createScrap() {
        val internshipAnnouncementId = 1L
        val userId = 1L
        every { scrapService.scrap(userId, internshipAnnouncementId, scrapRequest) } just runs

        mockMvc.post("/api/v1/scraps/$internshipAnnouncementId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(scrapRequest)
        }.andExpect {
            status { isCreated() }
        }
    }

    @Test
    @DisplayName("스크랩 색상을 업데이트한다")
    fun updateScrap() {
        val internshipAnnouncementId = 1L
        val userId = 1L
        every { scrapService.updateScrap(userId, internshipAnnouncementId, scrapUpdateRequest) } just runs

        mockMvc.patch("/api/v1/scraps/$internshipAnnouncementId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(scrapUpdateRequest)
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    @DisplayName("스크랩을 취소한다")
    fun cancelScrap() {
        val internshipAnnouncementId = 1L
        val userId = 1L
        every { scrapService.cancelScrap(userId, internshipAnnouncementId) } just runs

        mockMvc.delete("/api/v1/scraps/$internshipAnnouncementId") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }
    }
}
