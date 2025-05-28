package com.terning.server.kotlin.domain.filter.vo

import com.terning.server.kotlin.domain.filter.FilterErrorCode
import com.terning.server.kotlin.domain.filter.FilterException
import jakarta.persistence.Embeddable

@Embeddable
class FilterYear private constructor(
    val value: Int,
) {
    init {
        validateYear(value)
    }

    protected constructor() : this(2000)

    override fun equals(other: Any?): Boolean = this === other || (other is FilterYear && value == other.value)

    override fun hashCode(): Int = value

    override fun toString(): String = "$value"

    private fun validateYear(value: Int) {
        if (value <= MIN_VALID_YEAR) {
            throw FilterException(FilterErrorCode.INVALID_YEAR)
        }
    }

    companion object {
        private const val MIN_VALID_YEAR = 1900

        fun from(value: Int): FilterYear = FilterYear(value)
    }
}
