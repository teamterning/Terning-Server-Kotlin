package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

@Embeddable
class InternshipAnnouncementStartDate private constructor(
    @Embedded
    val year: InternshipAnnouncementYear,
    @Embedded
    val month: InternshipAnnouncementMonth,
) {
    companion object {
        fun of(
            year: InternshipAnnouncementYear,
            month: InternshipAnnouncementMonth,
        ): InternshipAnnouncementStartDate {
            return InternshipAnnouncementStartDate(year, month)
        }
    }

    override fun equals(other: Any?): Boolean {
        return this === other || (
            other is InternshipAnnouncementStartDate &&
                this.year == other.year && this.month == other.month
        )
    }

    override fun hashCode(): Int = 31 * year.hashCode() + month.hashCode()

    override fun toString(): String = "${year.value}년 ${month.value}월"
}
