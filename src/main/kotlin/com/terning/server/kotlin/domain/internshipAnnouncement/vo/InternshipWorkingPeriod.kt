package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import jakarta.persistence.Embeddable

@Embeddable
class InternshipWorkingPeriod private constructor(
    val months: Int,
) {
    init {
        validatePositive(months)
    }

    fun toKoreanPeriod(): String = "${months}개월"

    override fun equals(other: Any?): Boolean = this === other || (other is InternshipWorkingPeriod && months == other.months)

    override fun hashCode(): Int = months

    override fun toString(): String = toKoreanPeriod()

    private fun validatePositive(months: Int) {
        if (months < MINIMUM_MONTHS) {
            throw InternshipAnnouncementException(InternshipAnnouncementErrorCode.INVALID_WORKING_PERIOD)
        }
    }

    companion object {
        private const val MINIMUM_MONTHS = 1

        fun from(months: Int): InternshipWorkingPeriod = InternshipWorkingPeriod(months)
    }
}
