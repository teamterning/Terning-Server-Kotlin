package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Embeddable

@Embeddable
class ViewCount private constructor(
    val value: Int,
) {
    init {
        require(value >= MIN_VALUE) { INVALID_VIEW_COUNT_MESSAGE }
    }

    fun increase(): ViewCount = ViewCount(value + 1)

    override fun equals(other: Any?): Boolean = other is ViewCount && value == other.value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()

    companion object {
        private const val MIN_VALUE = 0
        private const val INVALID_VIEW_COUNT_MESSAGE = "조회수는 음수일 수 없습니다."

        fun from(): ViewCount = ViewCount(MIN_VALUE)
    }
}
