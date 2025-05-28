package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Embeddable

@Embeddable
class InternshipAnnouncementScrapCount private constructor(
    val value: Int,
) {
    init {
        validateMinimum()
    }

    protected constructor() : this(MIN_VALUE)

    fun increase(): InternshipAnnouncementScrapCount = InternshipAnnouncementScrapCount(value + 1)

    fun decrease(): InternshipAnnouncementScrapCount {
        if (value == MIN_VALUE) {
            throw InternshipException(InternshipErrorCode.SCRAP_COUNT_CANNOT_BE_DECREASED_BELOW_ZERO)
        }
        return InternshipAnnouncementScrapCount(value - 1)
    }

    override fun equals(other: Any?): Boolean = other is InternshipAnnouncementScrapCount && value == other.value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()

    private fun validateMinimum() {
        if (value < MIN_VALUE) {
            throw InternshipException(InternshipErrorCode.INVALID_SCRAP_COUNT)
        }
    }

    companion object {
        private const val MIN_VALUE = 0

        fun from(): InternshipAnnouncementScrapCount = InternshipAnnouncementScrapCount(MIN_VALUE)
    }
}
