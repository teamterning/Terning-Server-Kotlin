package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Embeddable

@Embeddable
class ViewCount private constructor(
    val value: Int,
) {
    init {
        if (value < MIN_VALUE) {
            throw InternshipException(InternshipErrorCode.INVALID_VIEW_COUNT)
        }
    }

    fun increase(): ViewCount = ViewCount(value + 1)

    override fun equals(other: Any?): Boolean = other is ViewCount && value == other.value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()

    companion object {
        private const val MIN_VALUE = 0

        fun from(): ViewCount = ViewCount(MIN_VALUE)
    }
}
