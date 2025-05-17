package com.terning.server.kotlin.domain.filter

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Year private constructor(
    @Column(nullable = false)
    val value: Int,
) {
    init {
        require(value > 1900) { INVALID_YEAR_MESSAGE.format(value) }
    }

    companion object {
        private const val INVALID_YEAR_MESSAGE = "연도는 1900보다 커야 합니다. 입력값: %d"

        fun from(value: Int): Year = Year(value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Year
        return value == other.value
    }

    override fun hashCode(): Int = value

    override fun toString(): String = "$value"
}
