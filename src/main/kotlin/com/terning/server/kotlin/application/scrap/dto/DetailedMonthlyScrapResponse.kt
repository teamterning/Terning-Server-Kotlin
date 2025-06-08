package com.terning.server.kotlin.application.scrap.dto

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class DetailedMonthlyScrapResponse(
    val dailyGroups: List<DetailedScrapGroup>,
)

data class DetailedScrapGroup(
    val deadline: String,
    val scraps: List<DetailedScrap>,
)

data class DetailedScrap(
    val announcementId: Long,
    val companyImageUrl: String,
    val title: String,
    val workingPeriod: String,
    val isScrapped: Boolean,
    val hexColor: String,
    val deadline: LocalDate,
    val startYear: Int,
    val startMonth: Int,
) {
    val formattedDeadline: String = formatToDday(deadline)
    val deadlineText: String = "${deadline.year}년 ${deadline.monthValue}월 ${deadline.dayOfMonth}일"
    val startYearMonth: String = "${startYear}년 ${startMonth}월"

    companion object {
        private fun formatToDday(deadline: LocalDate): String {
            val today = LocalDate.now()
            return when {
                deadline.isEqual(today) -> "D-DAY"
                deadline.isBefore(today) -> "지원마감"
                else -> "D-${ChronoUnit.DAYS.between(today, deadline)}"
            }
        }
    }
}
