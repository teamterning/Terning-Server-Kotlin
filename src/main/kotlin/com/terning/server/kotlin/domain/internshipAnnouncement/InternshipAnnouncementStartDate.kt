package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

@Embeddable
class InternshipAnnouncementStartDate private constructor(
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "start_year", nullable = false))
    val year: InternshipAnnouncementYear,
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "start_month", nullable = false))
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
