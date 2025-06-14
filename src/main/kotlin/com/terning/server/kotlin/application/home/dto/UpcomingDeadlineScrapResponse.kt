package com.terning.server.kotlin.application.home.dto

import com.terning.server.kotlin.domain.scrap.Scrap
import java.time.Clock
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class UpcomingDeadlineScrapResponse(
    val hasScrapped: Boolean,
    val message: String,
    val scraps: List<UpcomingDeadlineScrapDetail>,
)

data class UpcomingDeadlineScrapDetail(
    val internshipAnnouncementId: Long,
    val companyImage: String,
    val companyInfo: String,
    val title: String,
    val dDay: String,
    val workingPeriod: String,
    val isScrapped: Boolean,
    val color: String,
    val deadline: String,
    val startYearMonth: String,
) {
    companion object {
        fun of(
            scrap: Scrap,
            clock: Clock,
        ): UpcomingDeadlineScrapDetail {
            val announcement = scrap.internshipAnnouncement
            val today = LocalDate.now(clock)

            val deadlineDate = announcement.internshipAnnouncementDeadline.value
            val daysUntilDeadline = ChronoUnit.DAYS.between(today, deadlineDate)

            val dDayString =
                when {
                    daysUntilDeadline == 0L -> "D-DAY"
                    else -> "D-$daysUntilDeadline"
                }

            return UpcomingDeadlineScrapDetail(
                internshipAnnouncementId =
                    checkNotNull(announcement.id) {
                        "[ERROR] 스크랩(id=${scrap.id})이 id가 없는 InternshipAnnouncement를 참조하고 있습니다."
                    },
                companyImage = announcement.company.logoUrl.value,
                companyInfo = announcement.company.name.value,
                title = announcement.title.value,
                dDay = dDayString,
                workingPeriod = announcement.workingPeriod.toString(),
                isScrapped = true,
                color = scrap.hexColor(),
                deadline = deadlineDate.toString(),
                startYearMonth = "${announcement.startDate.year.value}년 ${announcement.startDate.month.value}월",
            )
        }
    }
}
