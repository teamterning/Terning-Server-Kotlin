package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import jakarta.persistence.Embeddable

@Embeddable
class InternshipAnnouncementMonth private constructor(
    val value: Int,
) {
    init {
        validateMonth(value)
    }

    override fun equals(other: Any?): Boolean = this === other || (other is InternshipAnnouncementMonth && value == other.value)

    override fun hashCode(): Int = value

    override fun toString(): String = value.toString()

    private fun validateMonth(value: Int) {
        if (value !in MIN_MONTH..MAX_MONTH) {
            throw InternshipAnnouncementException(InternshipAnnouncementErrorCode.INVALID_MONTH)
        }
    }

    companion object {
        private const val MIN_MONTH = 1
        private const val MAX_MONTH = 12

        fun from(value: Int): InternshipAnnouncementMonth = InternshipAnnouncementMonth(value)
    }
}
