package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

@Embeddable
class InternshipAnnouncementStartDate private constructor(
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "startYear", nullable = false))
    val year: InternshipAnnouncementYear,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "startMonth", nullable = false))
    val month: InternshipAnnouncementMonth,
) {

    override fun equals(other: Any?): Boolean =
        this === other || (
                other is InternshipAnnouncementStartDate &&
                        this.year == other.year && this.month == other.month
                )

    override fun hashCode(): Int = 31 * year.hashCode() + month.hashCode()

    override fun toString(): String = "${year.value}년 ${month.value}월"

    companion object {
        fun of(
            year: InternshipAnnouncementYear,
            month: InternshipAnnouncementMonth,
        ): InternshipAnnouncementStartDate =
            InternshipAnnouncementStartDate(year, month)
    }
}
