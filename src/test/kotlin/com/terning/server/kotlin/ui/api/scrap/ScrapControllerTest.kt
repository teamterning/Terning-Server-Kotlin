package com.terning.server.kotlin.ui.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.terning.server.kotlin.application.ScrapService
import com.terning.server.kotlin.application.scrap.dto.MonthlyScrapDeadLineSummary
import com.terning.server.kotlin.application.scrap.dto.MonthlyScrapDeadlineGroup
import com.terning.server.kotlin.application.scrap.dto.MonthlyScrapDeadlineResponse
import com.terning.server.kotlin.application.scrap.dto.ScrapRequest
import com.terning.server.kotlin.application.scrap.dto.ScrapUpdateRequest
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
    @DisplayName("월간 스크랩 데이터를 조회한다")
    fun getMonthlyScraps() {
        // given
        val userId = 1L
        val year = 2025
        val month = 6

        val summary =
            MonthlyScrapDeadLineSummary(
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

        // when & then
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
