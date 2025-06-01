package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import jakarta.persistence.Embeddable

@Embeddable
class InternshipAnnouncementViewCount private constructor(
    val value: Int,
) {
    init {
        validateMinimum(value)
    }

    fun increase(): InternshipAnnouncementViewCount = InternshipAnnouncementViewCount(value + 1)

    override fun equals(other: Any?): Boolean = other is InternshipAnnouncementViewCount && value == other.value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()

    private fun validateMinimum(value: Int) {
        if (value < MIN_VALUE) {
            throw InternshipAnnouncementException(InternshipAnnouncementErrorCode.INVALID_VIEW_COUNT)
        }
    }

    companion object {
        private const val MIN_VALUE = 0

        fun from(): InternshipAnnouncementViewCount = InternshipAnnouncementViewCount(MIN_VALUE)
    }
}
