package com.terning.server.kotlin.ui.api

import com.ninjasquad.springmockk.MockkBean
import com.terning.server.kotlin.application.announcement.AnnouncementService
import com.terning.server.kotlin.application.announcement.dto.DetailAnnouncementResponse
import io.mockk.every
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(AnnouncementController::class)
@ActiveProfiles("test")
class AnnouncementControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var announcementService: AnnouncementService

    @Test
    @DisplayName("사용자가 원하는 특정 인턴공고 상세 페이지를 조회한다")
    fun getDetailInternshipAnnouncement() {
        // given
        val internshipAnnouncementId = 1L
        val userId = 1L
        val response =
            DetailAnnouncementResponse(
                companyImage = "https://media-cdn.linkareer.com/activity_manager/logos/467093",
                dDay = "D-4",
                title = "[카카오페이] 가맹점 업무 지원 어시스턴트 채용",
                workingPeriod = "6개월",
                isScrapped = true,
                color = "#F3A649",
                deadline = "2025-07-01",
                startYearMonth = "2025-08",
                scrapCount = 2,
                viewCount = 5,
                company = "카카오페이",
                companyCategory = "대기엽/중견기업",
                qualification = "졸업 예정자, 휴학생 가능",
                jobType = "IT",
                detail = "[지원자격] 성실하고 꼼꼼한 성격을 소유하신 분이면 좋겠어요.",
                url = "https://kakaopay.career.greetinghr.com/ko/o/136662",
            )

        every {
            announcementService.getDetailAnnouncement(
                userId = userId,
                internshipAnnouncementId = internshipAnnouncementId,
            )
        } returns response

        // when & then
        mockMvc.get("/api/v1/announcements/$internshipAnnouncementId")
            .andExpect {
                status { isOk() }
                jsonPath("$.status") { value(200) }
                jsonPath("$.message") { value("공고 상세 정보 불러오기에 성공했습니다") }
                jsonPath("$.result.companyImage") { value("https://media-cdn.linkareer.com/activity_manager/logos/467093") }
                jsonPath("$.result.dday") { value("D-4") }
                jsonPath("$.result.title") { value("[카카오페이] 가맹점 업무 지원 어시스턴트 채용") }
                jsonPath("$.result.workingPeriod") { value("6개월") }
                jsonPath("$.result.isScrapped") { value(true) }
                jsonPath("$.result.color") { value("#F3A649") }
                jsonPath("$.result.deadline") { value("2025-07-01") }
                jsonPath("$.result.startYearMonth") { value("2025-08") }
                jsonPath("$.result.scrapCount") { value(2) }
                jsonPath("$.result.viewCount") { value(5) }
                jsonPath("$.result.company") { value("카카오페이") }
                jsonPath("$.result.companyCategory") { value("대기엽/중견기업") }
                jsonPath("$.result.qualification") { value("졸업 예정자, 휴학생 가능") }
                jsonPath("$.result.jobType") { value("IT") }
                jsonPath("$.result.detail") { value("[지원자격] 성실하고 꼼꼼한 성격을 소유하신 분이면 좋겠어요.") }
                jsonPath("$.result.url") { value("https://kakaopay.career.greetinghr.com/ko/o/136662") }
            }
    }
}
