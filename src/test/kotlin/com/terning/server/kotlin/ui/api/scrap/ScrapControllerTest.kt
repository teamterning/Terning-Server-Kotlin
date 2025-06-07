package com.terning.server.kotlin.ui.api.scrap

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.terning.server.kotlin.application.scrap.ScrapRequest
import com.terning.server.kotlin.application.scrap.ScrapService
import com.terning.server.kotlin.application.scrap.ScrapUpdateRequest
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
