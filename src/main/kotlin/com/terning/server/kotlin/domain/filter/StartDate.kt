package com.terning.server.kotlin.domain.filter

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

@Embeddable
class StartDate private constructor(
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "start_year"))
    val year: Year,
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "start_month"))
    val month: Month,
) {
    companion object {
        fun of(
            year: Year,
            month: Month,
        ): StartDate = StartDate(year, month)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as StartDate
        return year == other.year && month == other.month
    }

    override fun hashCode(): Int = 31 * year.hashCode() + month.hashCode()

    override fun toString(): String = "${year.value}년 ${month.value}월"
}
