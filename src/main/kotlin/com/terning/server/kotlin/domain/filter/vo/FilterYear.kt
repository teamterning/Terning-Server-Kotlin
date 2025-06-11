package com.terning.server.kotlin.domain.filter.vo

import com.terning.server.kotlin.domain.filter.exception.FilterErrorCode
import com.terning.server.kotlin.domain.filter.exception.FilterException
import jakarta.persistence.Embeddable

@Embeddable
class FilterYear private constructor(val value: Int) {
    init {
        validateYear(value)
    }

    private fun validateYear(value: Int) {
        if (value < MIN_VALID_YEAR) {
            throw FilterException(FilterErrorCode.INVALID_YEAR)
        }
    }

    override fun equals(other: Any?): Boolean = this === other || (other is FilterYear && value == other.value)

    override fun hashCode(): Int = value

    override fun toString(): String = "$value"

    companion object {
        private const val MIN_VALID_YEAR = 1900
        val DEFAULT = FilterYear(MIN_VALID_YEAR)

        fun from(value: Int): FilterYear = FilterYear(value)
    }
}
