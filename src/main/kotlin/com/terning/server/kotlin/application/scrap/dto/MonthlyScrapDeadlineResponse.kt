package com.terning.server.kotlin.application.scrap.dto

data class MonthlyScrapDeadlineResponse(
    val monthlyScrapDeadline: List<MonthlyScrapDeadlineGroup>,
)

data class MonthlyScrapDeadlineGroup(
    val deadline: String,
    val scraps: List<MonthlyScrapDeadlineSummary>,
)

data class MonthlyScrapDeadlineSummary(
    val scrapId: Long,
    val title: String,
    val color: String,
)
