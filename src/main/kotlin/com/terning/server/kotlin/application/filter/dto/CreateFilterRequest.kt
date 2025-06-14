package com.terning.server.kotlin.application.filter.dto

data class CreateFilterRequest(
    val grade: String,
    val workingPeriod: String,
    val startYear: Int,
    val startMonth: Int,
)
