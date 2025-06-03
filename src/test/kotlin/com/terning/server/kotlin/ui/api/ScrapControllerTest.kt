package com.terning.server.kotlin.ui.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.terning.server.kotlin.application.ScrapService
import com.terning.server.kotlin.application.scrap.ScrapRequest
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

    @BeforeEach
    fun setUp() {
        scrapRequest = ScrapRequest(color = "BLUE")
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
}
