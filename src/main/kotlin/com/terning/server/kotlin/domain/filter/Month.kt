package com.terning.server.kotlin.domain.filter

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Month private constructor(
    @Column(name = "start_month", nullable = false)
    val value: Int,
) {
    init {
        require(value in 1..12) { INVALID_MONTH_MESSAGE.format(value) }
    }

    companion object {
        private const val INVALID_MONTH_MESSAGE = "월은 1~12 사이여야 합니다. 입력값: %d"

        fun from(value: Int): Month = Month(value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Month
        return value == other.value
    }

    override fun hashCode(): Int = value

    override fun toString(): String = "$value"
}
