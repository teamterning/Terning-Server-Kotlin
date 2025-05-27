package com.terning.server.kotlin.domain.filter

import jakarta.persistence.Embeddable

@Embeddable
class FilterMonth private constructor(
    val value: Int,
) {

    protected constructor() : this(MIN_MONTH)

    init {
        validateRange(value)
    }

    override fun equals(other: Any?): Boolean =
        this === other || (other is FilterMonth && value == other.value)

    override fun hashCode(): Int = value

    override fun toString(): String = "$value"

    private fun validateRange(value: Int) {
        if (value !in MIN_MONTH..MAX_MONTH) {
            throw IllegalArgumentException(INVALID_MONTH_MESSAGE.format(value))
        }
    }

    companion object {
        private const val MIN_MONTH = 1
        private const val MAX_MONTH = 12
        private const val INVALID_MONTH_MESSAGE = "월은 $MIN_MONTH~$MAX_MONTH 사이여야 합니다. 입력값: %d"

        fun from(value: Int): FilterMonth = FilterMonth(value)
    }
}
