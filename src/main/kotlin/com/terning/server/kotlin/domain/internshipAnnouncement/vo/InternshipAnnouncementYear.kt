package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipException
import jakarta.persistence.Embeddable

@Embeddable
class InternshipAnnouncementYear private constructor(
    val value: Int,
) {
    init {
        validateYear(value)
    }

    protected constructor() : this(MIN_VALID_YEAR + 1)

    override fun equals(other: Any?): Boolean = this === other || (other is InternshipAnnouncementYear && value == other.value)

    override fun hashCode(): Int = value

    override fun toString(): String = value.toString()

    private fun validateYear(value: Int) {
        if (value <= MIN_VALID_YEAR) {
            throw InternshipException(InternshipErrorCode.INVALID_YEAR)
        }
    }

    companion object {
        private const val MIN_VALID_YEAR = 2024

        fun from(value: Int): InternshipAnnouncementYear = InternshipAnnouncementYear(value)
    }
}
