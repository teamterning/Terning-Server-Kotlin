package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Embeddable

@Embeddable
class InternshipWorkingPeriod private constructor(
    val months: Int,
) {
    init {
        validatePositive(months)
    }

    companion object {
        fun from(months: Int): InternshipWorkingPeriod {
            return InternshipWorkingPeriod(months)
        }

        private fun validatePositive(months: Int) {
            if (months <= 0) {
                throw InternshipException(InternshipErrorCode.INVALID_WORKING_PERIOD)
            }
        }
    }

    fun toKoreanPeriod(): String = "${months}개월"

    override fun equals(other: Any?): Boolean = this === other || (other is InternshipWorkingPeriod && months == other.months)

    override fun hashCode(): Int = months

    override fun toString(): String = toKoreanPeriod()
}
