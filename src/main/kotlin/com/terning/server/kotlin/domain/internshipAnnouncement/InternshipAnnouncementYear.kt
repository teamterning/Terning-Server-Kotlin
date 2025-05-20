package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Embeddable

@Embeddable
class InternshipAnnouncementYear private constructor(
    val value: Int,
) {
    init {
        validateYear(value)
    }

    companion object {
        private const val MIN_VALID_YEAR = 2025

        fun from(value: Int): InternshipAnnouncementYear = InternshipAnnouncementYear(value)

        private fun validateYear(value: Int) {
            if (value < MIN_VALID_YEAR) {
                throw InternshipException(InternshipErrorCode.INVALID_YEAR)
            }
        }
    }

    override fun equals(other: Any?): Boolean = this === other || (other is InternshipAnnouncementYear && value == other.value)

    override fun hashCode(): Int = value

    override fun toString(): String = value.toString()
}
