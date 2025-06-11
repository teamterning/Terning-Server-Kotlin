package com.terning.server.kotlin.ui.api.filter

import com.ninjasquad.springmockk.MockkBean
import com.terning.server.kotlin.application.filter.FilterService
import com.terning.server.kotlin.application.filter.dto.FilterResponse
import com.terning.server.kotlin.ui.api.FilterController
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(FilterController::class)
@ActiveProfiles("test")
class FilterControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var filterService: FilterService

    private lateinit var filterResponse: FilterResponse

    @BeforeEach
    fun setUp() {
        filterResponse =
            FilterResponse(
                jobType = "it",
                grade = "senior",
                workingPeriod = "short",
                startYear = 2025,
                startMonth = 6,
            )
    }

    @Test
    @DisplayName("필터링 정보를 가져온다")
    fun getFilter() {
        // given
        val userId = 1L
        every { filterService.getUserFilter(userId = userId) } returns filterResponse

        // when
        mockMvc.get("/api/v1/filters") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            // then
            status { isOk() }
            jsonPath("$.result.jobType") { value("it") }
            jsonPath("$.result.grade") { value("senior") }
            jsonPath("$.result.workingPeriod") { value("short") }
            jsonPath("$.result.startYear") { value(2025) }
            jsonPath("$.result.startMonth") { value(6) }
        }
    }
}
