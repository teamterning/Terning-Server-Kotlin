package com.terning.server.kotlin.application.filtering

data class FilteringResponse (
    val jobType: String,
    val grade: String,
    val workingPeriod: String,
    val startYear: Int,
    val startMonth: Int,
)
