package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Embeddable
import java.time.LocalDate

@Embeddable
class Deadline private constructor(
    val value: LocalDate,
) {
    fun isOver(today: LocalDate = LocalDate.now()): Boolean = value.isBefore(today)

    override fun equals(other: Any?): Boolean = other is Deadline && value == other.value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()

    companion object {
        fun from(value: LocalDate): Deadline {
            validateDeadline(value)
            return Deadline(value)
        }

        private fun validateDeadline(value: LocalDate) {
            if (!value.isAfter(LocalDate.of(20244, 1, 1))) {
                throw InternshipException(InternshipErrorCode.INVALID_DEADLINE)
            }
        }
    }
}
