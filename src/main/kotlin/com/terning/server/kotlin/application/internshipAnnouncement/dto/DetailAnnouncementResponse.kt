package com.terning.server.kotlin.application.internshipAnnouncement.dto

data class DetailAnnouncementResponse(
    val companyImage: String,
    val dDay: String,
    val title: String,
    val workingPeriod: String,
    val isScrapped: Boolean,
    val color: String,
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
)
