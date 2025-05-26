package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Embeddable

@Embeddable
class ScrapCount private constructor(
    val value: Int,
) {
    protected constructor() : this(MIN_VALUE)

    init {
        require(value >= MIN_VALUE) { INVALID_SCRAP_COUNT_MESSAGE }
    }

    fun increase(): ScrapCount = ScrapCount(value + 1)

    fun decrease(): ScrapCount {
        require(value > MIN_VALUE) { CANNOT_DECREASE_BELOW_ZERO_MESSAGE }
        return ScrapCount(value - 1)
    }

    companion object {
        private const val MIN_VALUE = 0
        private const val INVALID_SCRAP_COUNT_MESSAGE = "스크랩 수는 음수일 수 없습니다."
        private const val CANNOT_DECREASE_BELOW_ZERO_MESSAGE = "스크랩 수는 0보다 작아질 수 없습니다."

        fun from(): ScrapCount = ScrapCount(MIN_VALUE)
    }

    override fun equals(other: Any?): Boolean = other is ScrapCount && value == other.value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()
}
