package com.terning.server.kotlin.application.internshipAnnouncement.dto

import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementMonth
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementStartDate
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementYear
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipWorkingPeriod
import java.time.Clock
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class DetailAnnouncementResponse(
    val companyImage: String,
    val dDay: String,
    val title: String,
    val workingPeriod: String,
    val isScrapped: Boolean,
    val color: String?,
    val deadline: String,
    val startYearMonth: String,
    val scrapCount: Int,
    val viewCount: Int,
    val company: String,
    val companyCategory: String,
    val qualification: String,
    val jobType: String,
    val detail: String,
    val url: String,
) {
    companion object {
        fun from(
            companyImage: String,
            deadline: LocalDate,
            clock: Clock,
            title: String,
            workingPeriod: Int,
            isScrapped: Boolean,
            color: String?,
            startYear: Int,
            startMonth: Int,
            scrapCount: Int,
            viewCount: Int,
            company: String,
            companyCategory: String,
            qualification: String,
            jobType: String,
            detail: String,
            url: String,
        ): DetailAnnouncementResponse {
            val today = LocalDate.now(clock)
            val dDay =
                when {
                    deadline.isEqual(today) -> "D-DAY"
                    deadline.isBefore(today) -> "지원마감"
                    else -> "D-${ChronoUnit.DAYS.between(today, deadline)}"
                }

            return DetailAnnouncementResponse(
                companyImage = companyImage,
                dDay = dDay,
                title = title,
                workingPeriod = InternshipWorkingPeriod.from(workingPeriod).toKoreanPeriod(),
                isScrapped = isScrapped,
                color = color,
                deadline = deadline.toString(),
                startYearMonth =
                    InternshipAnnouncementStartDate.of(
                        InternshipAnnouncementYear.from(startYear),
                        InternshipAnnouncementMonth.from(startMonth),
                    ).toString(),
                scrapCount = scrapCount,
                viewCount = viewCount,
                company = company,
                companyCategory = companyCategory,
                qualification = qualification,
                jobType = jobType,
                detail = detail,
                url = url,
            )
        }
    }
}
