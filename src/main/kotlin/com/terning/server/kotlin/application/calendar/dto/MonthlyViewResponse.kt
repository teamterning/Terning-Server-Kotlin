package com.terning.server.kotlin.application.calendar.dto

data class MonthlyViewResponse(
    val deadlines: List<DeadlineGroup>,
) {
    data class DeadlineGroup(
        val deadline: String,
        val scraps: List<ScrapSummary>,
    )

    data class ScrapSummary(
        val scrapId: Long,
        val title: String,
        val color: String,
    )
}
