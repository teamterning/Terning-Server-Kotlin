package com.terning.server.kotlin.application.filter.dto

data class FilterRequest(
    val jobType: String,
    val grade: String,
    val workingPeriod: String,
    val startYear: Int,
    val startMonth: Int,
)
