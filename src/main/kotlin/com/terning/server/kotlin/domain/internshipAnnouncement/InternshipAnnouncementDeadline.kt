package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Embeddable
import java.time.LocalDate

@Embeddable
class InternshipAnnouncementDeadline private constructor(
    val value: LocalDate,
) {
    protected constructor() : this(LocalDate.of(2024, 1, 2))

    fun isOver(today: LocalDate = LocalDate.now()): Boolean = value.isBefore(today)

    override fun equals(other: Any?): Boolean = other is InternshipAnnouncementDeadline && value == other.value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()

    private fun validateDeadline(value: LocalDate) {
        if (!value.isAfter(MIN_DEADLINE_DATE)) {
            throw InternshipException(InternshipErrorCode.INVALID_DEADLINE)
        }
    }

    companion object {
        private val MIN_DEADLINE_DATE: LocalDate = LocalDate.of(2024, 1, 1)

        fun from(value: LocalDate): InternshipAnnouncementDeadline {
            val internshipAnnouncementDeadline = InternshipAnnouncementDeadline(value)
            internshipAnnouncementDeadline.validateDeadline(value)
            return internshipAnnouncementDeadline
        }
    }
}
