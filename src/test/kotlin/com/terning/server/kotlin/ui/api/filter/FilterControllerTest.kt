package com.terning.server.kotlin.ui.api.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.terning.server.kotlin.application.filter.FilterService
import com.terning.server.kotlin.application.filter.dto.CreateFilterRequest
import com.terning.server.kotlin.application.filter.dto.GetFilterResponse
import com.terning.server.kotlin.application.filter.dto.UpdateFilterRequest
import com.terning.server.kotlin.ui.api.FilterController
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
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@WebMvcTest(FilterController::class)
@ActiveProfiles("test")
class FilterControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var filterService: FilterService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var createFilterRequest: CreateFilterRequest
    private lateinit var getFilterResponse: GetFilterResponse
    private lateinit var updateFilterRequest: UpdateFilterRequest

    private val userId = 1L

    @BeforeEach
    fun setUp() {
        createFilterRequest =
            CreateFilterRequest(
                grade = "freshman",
                workingPeriod = "long",
                startYear = 2025,
                startMonth = 7,
            )

        getFilterResponse =
            GetFilterResponse(
                jobType = "it",
                grade = "senior",
                workingPeriod = "short",
                startYear = 2025,
                startMonth = 6,
            )

        updateFilterRequest =
            UpdateFilterRequest(
                jobType = "plan",
                grade = "sophomore",
                workingPeriod = "middle",
                startYear = 2025,
                startMonth = 2,
            )
    }

    @Test
    @DisplayName("필터링 정보를 생성한다")
    fun createUserFilter() {
        // given
        every {
            filterService.createUserFilter(
                userId = userId,
                createFilterRequest = createFilterRequest,
            )
        } just runs

        // when
        mockMvc.post("/api/v1/auth/sign-up/filter") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createFilterRequest)
        }.andExpect {
            // then
            status { isOk() }
        }
    }

    @Test
    @DisplayName("필터링 정보를 가져온다")
    fun getFilter() {
        // given
        every { filterService.getUserFilter(userId = userId) } returns getFilterResponse

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

    @Test
    @DisplayName("사용자가 요청한 필터링 정보를 저장한다")
    fun updateUserFilter() {
        // given
        every {
            filterService.updateUserFilter(
                userId = userId,
                updateFilterRequest = updateFilterRequest,
            )
        } just runs

        // when
        mockMvc.put("/api/v1/filters") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updateFilterRequest)
        }.andExpect {
            // then
            status { isOk() }
        }
    }
}
