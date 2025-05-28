package com.terning.server.kotlin.domain.filter.vo

import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

@Embeddable
class FilterStartDate private constructor(
    @Embedded
    val filterYear: FilterYear,
    @Embedded
    val filterMonth: FilterMonth,
) {
    protected constructor() : this(
        FilterYear.from(2000),
        FilterMonth.from(1),
    )

    override fun equals(other: Any?): Boolean =
        this === other || (
            other is FilterStartDate &&
                filterYear == other.filterYear &&
                filterMonth == other.filterMonth
        )

    override fun hashCode(): Int = 31 * filterYear.hashCode() + filterMonth.hashCode()

    override fun toString(): String = "${filterYear.value}년 ${filterMonth.value}월"

    companion object {
        fun of(
            filterYear: FilterYear,
            filterMonth: FilterMonth,
        ): FilterStartDate = FilterStartDate(filterYear, filterMonth)
    }
}
