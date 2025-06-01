package com.terning.server.kotlin.domain.filter.vo

import com.terning.server.kotlin.domain.filter.exception.FilterErrorCode
import com.terning.server.kotlin.domain.filter.exception.FilterException

enum class FilterWorkingPeriod(
    val period: String,
    val label: String,
) {
    SHORT_TERM("short", "1개월 ~ 3개월"),
    MID_TERM("middle", "4개월 ~ 6개월"),
    LONG_TERM("long", "7개월 이상"),
    ;

    companion object {
        fun from(period: String): FilterWorkingPeriod =
            entries.firstOrNull { it.period == period }
                ?: throw FilterException(FilterErrorCode.INVALID_WORKING_PERIOD)
    }
}
