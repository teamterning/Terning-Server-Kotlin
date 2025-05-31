package com.terning.server.kotlin.domain.filter

import com.terning.server.kotlin.domain.common.BaseRootEntity
import com.terning.server.kotlin.domain.filter.vo.FilterGrade
import com.terning.server.kotlin.domain.filter.vo.FilterJobType
import com.terning.server.kotlin.domain.filter.vo.FilterStartDate
import com.terning.server.kotlin.domain.filter.vo.FilterWorkingPeriod
import jakarta.persistence.AttributeOverride
import jakarta.persistence.AttributeOverrides
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "filters")
class Filter private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    private var filterJobType: FilterJobType,

    @Enumerated(EnumType.STRING)
    private var filterGrade: FilterGrade,

    @Enumerated(EnumType.STRING)
    private var filterWorkingPeriod: FilterWorkingPeriod,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "filterYear.value", column = Column(name = "startYear", nullable = false)),
        AttributeOverride(name = "filterMonth.value", column = Column(name = "startMonth", nullable = false)),
    )
    private var filterStartDate: FilterStartDate,
) : BaseRootEntity() {
    fun update(
        newFilterJobType: FilterJobType,
        newFilterGrade: FilterGrade,
        newFilterWorkingPeriod: FilterWorkingPeriod,
        newFilterStartDate: FilterStartDate,
    ) {
        this.filterJobType = newFilterJobType
        this.filterGrade = newFilterGrade
        this.filterWorkingPeriod = newFilterWorkingPeriod
        this.filterStartDate = newFilterStartDate
    }

    fun jobType(): FilterJobType = filterJobType

    fun grade(): FilterGrade = filterGrade

    fun workingPeriod(): FilterWorkingPeriod = filterWorkingPeriod

    fun startDate(): FilterStartDate = filterStartDate

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Filter
        return id != null && id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0

    companion object {
        fun of(
            filterJobType: FilterJobType,
            filterGrade: FilterGrade,
            filterWorkingPeriod: FilterWorkingPeriod,
            filterStartDate: FilterStartDate,
        ): Filter =
            Filter(
                filterJobType = filterJobType,
                filterGrade = filterGrade,
                filterWorkingPeriod = filterWorkingPeriod,
                filterStartDate = filterStartDate,
            )
    }
}
