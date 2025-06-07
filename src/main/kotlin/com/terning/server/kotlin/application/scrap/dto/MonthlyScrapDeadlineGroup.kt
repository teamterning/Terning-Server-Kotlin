package com.terning.server.kotlin.application.scrap.dto

data class MonthlyScrapDeadlineGroup(
    val deadline: String,
    val scraps: List<MonthlyScrapDeadLineSummary>,
)
