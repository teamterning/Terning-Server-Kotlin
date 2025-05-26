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
        private val MIN_DEADLINE_DATE: LocalDate = LocalDate.of(2024, 1, 1)
        private val ERROR_MESSAGE = "마감일은 $MIN_DEADLINE_DATE 이후여야 합니다."

        fun from(value: LocalDate): Deadline {
            require(value.isAfter(MIN_DEADLINE_DATE)) { ERROR_MESSAGE }
            return Deadline(value)
        }
    }
}
