package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import jakarta.persistence.Embeddable

@Embeddable
class InternshipAnnouncementScrapCount private constructor(
    val value: Int,
) {
    init {
        validateMinimum()
    }

    fun increase(): InternshipAnnouncementScrapCount = InternshipAnnouncementScrapCount(value + 1)

    fun decrease(): InternshipAnnouncementScrapCount {
        if (value == MIN_VALUE) {
            throw InternshipAnnouncementException(InternshipAnnouncementErrorCode.SCRAP_COUNT_CANNOT_BE_DECREASED_BELOW_ZERO)
        }
        return InternshipAnnouncementScrapCount(value - 1)
    }

    override fun equals(other: Any?): Boolean = other is InternshipAnnouncementScrapCount && value == other.value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()

    private fun validateMinimum() {
        if (value < MIN_VALUE) {
            throw InternshipAnnouncementException(InternshipAnnouncementErrorCode.INVALID_SCRAP_COUNT)
        }
    }

    companion object {
        private const val MIN_VALUE = 0

        fun from(value: Int = MIN_VALUE): InternshipAnnouncementScrapCount = InternshipAnnouncementScrapCount(value)
    }
}
