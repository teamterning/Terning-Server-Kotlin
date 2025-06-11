package com.terning.server.kotlin.application.scrap.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Clock
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class DetailedMonthlyScrapResponse(
    val dailyGroups: List<DetailedScrapGroup>,
)

data class DetailedScrapGroup(
    val deadline: String,
    val scraps: List<DetailedScrap>,
)

data class DetailedScrap private constructor(
    private val rawAnnouncementId: Long,
    private val rawCompanyImageUrl: String,
    private val rawTitle: String,
    private val rawWorkingPeriod: String,
    private val rawIsScrapped: Boolean,
    private val rawHexColor: String,
    @get:JsonIgnore
    private val rawDeadline: LocalDate,
    @get:JsonIgnore
    private val rawStartYear: Int,
    @get:JsonIgnore
    private val rawStartMonth: Int,
    private val rawDDay: String,
) {
    val announcementId: Long = rawAnnouncementId
    val companyImageUrl: String = rawCompanyImageUrl
    val dDay: String = rawDDay
    val title: String = rawTitle
    val workingPeriod: String = rawWorkingPeriod
    val isScrapped: Boolean = rawIsScrapped
    val hexColor: String = rawHexColor
    val deadline: String = "${rawDeadline.year}년 ${rawDeadline.monthValue}월 ${rawDeadline.dayOfMonth}일"
    val startYearMonth: String = "${rawStartYear}년 ${rawStartMonth}월"

    companion object {
        fun from(
            announcementId: Long,
            companyImageUrl: String,
            title: String,
            workingPeriod: String,
            isScrapped: Boolean,
            hexColor: String,
            deadline: LocalDate,
            startYear: Int,
            startMonth: Int,
            clock: Clock,
        ): DetailedScrap {
            val today = LocalDate.now(clock)
            val dDay =
                when {
                    deadline.isEqual(today) -> "D-DAY"
                    deadline.isBefore(today) -> "지원마감"
                    else -> "D-${ChronoUnit.DAYS.between(today, deadline)}"
                }

            return DetailedScrap(
                rawAnnouncementId = announcementId,
                rawCompanyImageUrl = companyImageUrl,
                rawTitle = title,
                rawWorkingPeriod = workingPeriod,
                rawIsScrapped = isScrapped,
                rawHexColor = hexColor,
                rawDeadline = deadline,
                rawStartYear = startYear,
                rawStartMonth = startMonth,
                rawDDay = dDay,
            )
        }
    }
}
