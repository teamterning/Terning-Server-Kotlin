package com.terning.server.kotlin.domain.filter.vo

import com.terning.server.kotlin.domain.filter.exception.FilterErrorCode
import com.terning.server.kotlin.domain.filter.exception.FilterException

enum class FilterGrade(
    val type: String,
    val label: String,
) {
    NONE("none", "선택 안 함"),
    FRESHMAN("freshman", "1학년"),
    SOPHOMORE("sophomore", "2학년"),
    JUNIOR("junior", "3학년"),
    SENIOR("senior", "4학년"),
    ;

    companion object {
        fun from(type: String): FilterGrade =
            entries.firstOrNull { it.type.equals(type, ignoreCase = true) }
                ?: throw FilterException(FilterErrorCode.INVALID_GRADE)
    }
}
