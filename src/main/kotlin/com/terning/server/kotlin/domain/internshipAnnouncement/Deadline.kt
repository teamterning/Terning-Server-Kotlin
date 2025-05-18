package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Embeddable
import java.time.LocalDate

@Embeddable
class Deadline private constructor(
    val value: LocalDate
) {

    companion object {
        fun from(value: LocalDate): Deadline {
            require(value.isAfter(LocalDate.of(2025, 1, 1))) {
                "마감일은 2025년 이후여야 합니다."
            }
            return Deadline(value)
        }
    }

    fun isOver(today: LocalDate = LocalDate.now()): Boolean {
        return value.isBefore(today)
    }

    override fun equals(other: Any?): Boolean =
        other is Deadline && value == other.value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()
}
