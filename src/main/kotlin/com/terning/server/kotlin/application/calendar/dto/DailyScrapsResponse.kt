package com.terning.server.kotlin.application.calendar.dto

import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncement
import com.terning.server.kotlin.domain.scrap.exception.ScrapErrorCode
import com.terning.server.kotlin.domain.scrap.exception.ScrapException
import java.time.Clock
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class DailyScrapsResponse(
    val announcementId: Long,
    val companyImageUrl: String,
    val dDay: String,
    val title: String,
    val workingPeriod: String,
    val isScrapped: Boolean,
    val hexColor: String,
    val deadline: String,
    val startYearMonth: String,
) {
    companion object {
        fun from(
            announcement: InternshipAnnouncement,
            isScrapped: Boolean,
            hexColor: String,
            clock: Clock,
        ): DailyScrapsResponse {
            val today = LocalDate.now(clock)
            val deadlineDate = announcement.internshipAnnouncementDeadline.value

            val dDay =
                when {
                    deadlineDate.isEqual(today) -> "D-DAY"
                    deadlineDate.isBefore(today) -> "지원마감"
                    else -> "D-${ChronoUnit.DAYS.between(today, deadlineDate)}"
                }

            val deadline = "${deadlineDate.year}년 ${deadlineDate.monthValue}월 ${deadlineDate.dayOfMonth}일"
            val startYearMonth = "${announcement.startDate.year.value}년 ${announcement.startDate.month.value}월"

            return DailyScrapsResponse(
                announcementId = announcement.id ?: throw ScrapException(ScrapErrorCode.SCRAP_ID_NULL),
                companyImageUrl = announcement.company.logoUrl.value,
                dDay = dDay,
                title = announcement.title.value,
                workingPeriod = announcement.workingPeriod.toString(),
                isScrapped = isScrapped,
                hexColor = hexColor,
                deadline = deadline,
                startYearMonth = startYearMonth,
            )
        }
    }
}
