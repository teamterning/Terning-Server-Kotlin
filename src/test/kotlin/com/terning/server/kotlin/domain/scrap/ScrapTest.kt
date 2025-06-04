package com.terning.server.kotlin.domain.scrap

import com.terning.server.kotlin.domain.filter.vo.FilterJobType
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncement
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.Company
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.CompanyCategory
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.CompanyLogoUrl
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.CompanyName
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementDeadline
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementMonth
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementStartDate
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementUrl
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementYear
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipTitle
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipWorkingPeriod
import com.terning.server.kotlin.domain.scrap.vo.Color
import com.terning.server.kotlin.domain.user.User
import com.terning.server.kotlin.domain.user.vo.UserState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ScrapTest {
    private val user = User.of("testUser", "smart", UserState.ACTIVE)

    private val internshipAnnouncement: InternshipAnnouncement =
        run {
            val title = InternshipTitle.from("백엔드 인턴 모집")
            val deadline = InternshipAnnouncementDeadline.from(LocalDate.of(2025, 6, 30))
            val workingPeriod = InternshipWorkingPeriod.from(3)
            val startDate =
                InternshipAnnouncementStartDate.of(
                    InternshipAnnouncementYear.from(2025),
                    InternshipAnnouncementMonth.from(7),
                )
            val url = InternshipAnnouncementUrl.from("https://example.com/internship")
            val company =
                Company.of(
                    name = CompanyName.from("터닝"),
                    category = CompanyCategory.from("대기업/중견기업"),
                    logoUrl = CompanyLogoUrl.from("https://example.com/logo.png"),
                )
            val jobType = FilterJobType.from("total")

            InternshipAnnouncement.of(
                title = title,
                deadline = deadline,
                workingPeriod = workingPeriod,
                startDate = startDate,
                url = url,
                company = company,
                jobType = jobType,
                qualifications = "자바 가능자 우대",
                detail = "스프링부트 기반 개발 참여",
                isGraduating = true,
            )
        }

    @Test
    @DisplayName("다른 색상으로 변경하면 성공한다")
    fun updateColorSuccess() {
        val scrap = Scrap.of(user, internshipAnnouncement, Color.BLUE)

        scrap.updateColor(Color.RED)

        assertEquals(Color.RED.toHexString(), scrap.hexColor())
    }
}
