package com.terning.server.kotlin.ui.api.internshipAnnouncemen

import com.ninjasquad.springmockk.MockkBean
import com.terning.server.kotlin.application.internshipAnnouncement.InternshipAnnouncementService
import com.terning.server.kotlin.application.internshipAnnouncement.dto.HomeAnnouncement
import com.terning.server.kotlin.application.internshipAnnouncement.dto.HomeAnnouncementsResponse
import com.terning.server.kotlin.ui.api.InternshipAnnouncementController
import io.mockk.every
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(InternshipAnnouncementController::class)
@ActiveProfiles("test")
class InternshipAnnouncementControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var internshipAnnouncementService: InternshipAnnouncementService

    @Test
    @DisplayName("필터링 조건에 맞는 인턴 공고를 조회한다")
    fun getInternshipAnnouncementsFilteredByUserFilter() {
        // given
        val pageable: Pageable = PageRequest.of(0, 10)
        val userId = 1L
        val sortBy = "deadlineSoon"

        val announcement =
            HomeAnnouncement(
                announcementId = 1L,
                companyImageUrl = "https://test.image/logo.png",
                title = "백엔드 인턴 모집",
                workingPeriod = "3개월",
                isScrapped = false,
                hexColor = "#FFFFFF",
            )
        val response =
            HomeAnnouncementsResponse(
                totalPages = 1,
                totalCount = 1L,
                hasNext = false,
                announcements = listOf(announcement),
            )

        every { internshipAnnouncementService.getFilteredAnnouncements(userId, sortBy, pageable) } returns response

        // when & then
        mockMvc.get("/api/v1/home") {
            param("sortBy", sortBy)
        }.andExpect {
            status { isOk() }
            jsonPath("$.status") { value(200) }
            jsonPath("$.message") { value("인턴 공고 불러오기를 성공했습니다") }
            jsonPath("$.result.totalPages") { value(1) }
            jsonPath("$.result.totalCount") { value(1) }
            jsonPath("$.result.hasNext") { value(false) }
            jsonPath("$.result.announcements[0].announcementId") { value(1) }
            jsonPath("$.result.announcements[0].title") { value("백엔드 인턴 모집") }
            jsonPath("$.result.announcements[0].companyImageUrl") { value("https://test.image/logo.png") }
            jsonPath("$.result.announcements[0].workingPeriod") { value("3개월") }
            jsonPath("$.result.announcements[0].isScrapped") { value(false) }
            jsonPath("$.result.announcements[0].hexColor") { value("#FFFFFF") }
        }
    }
}
