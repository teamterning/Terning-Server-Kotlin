package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import jakarta.persistence.Embeddable

@Embeddable
class InternshipAnnouncementYear private constructor(
    val value: Int,
) {
    init {
        validateYear(value)
    }

    override fun equals(other: Any?): Boolean = this === other || (other is InternshipAnnouncementYear && value == other.value)

    override fun hashCode(): Int = value

    override fun toString(): String = value.toString()

    private fun validateYear(value: Int) {
        if (value <= MIN_VALID_YEAR) {
            throw InternshipAnnouncementException(InternshipAnnouncementErrorCode.INVALID_YEAR)
        }
    }

    companion object {
        private const val MIN_VALID_YEAR = 2024

        fun from(value: Int): InternshipAnnouncementYear = InternshipAnnouncementYear(value)
    }
}
