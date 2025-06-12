package com.terning.server.kotlin.domain.filter.vo

import com.terning.server.kotlin.domain.filter.exception.FilterErrorCode
import com.terning.server.kotlin.domain.filter.exception.FilterException
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipWorkingPeriod

enum class FilterWorkingPeriod(
    val period: String,
    val label: String,
) {
    NONE("none", "선택 안 함") {
        override fun toInternshipWorkingPeriod() = InternshipWorkingPeriod.from(1)
    },
    SHORT_TERM("short", "1개월 ~ 3개월") {
        override fun toInternshipWorkingPeriod() = InternshipWorkingPeriod.from(3)
    },
    MID_TERM("middle", "4개월 ~ 6개월") {
        override fun toInternshipWorkingPeriod() = InternshipWorkingPeriod.from(6)
    },
    LONG_TERM("long", "7개월 이상") {
        override fun toInternshipWorkingPeriod() = InternshipWorkingPeriod.from(12)
    }, ;

    abstract fun toInternshipWorkingPeriod(): InternshipWorkingPeriod

    companion object {
        fun from(period: String): FilterWorkingPeriod =
            entries.firstOrNull { it.period == period }
                ?: throw FilterException(FilterErrorCode.INVALID_WORKING_PERIOD)
    }
}
