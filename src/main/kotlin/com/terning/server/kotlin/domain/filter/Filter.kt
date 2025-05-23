package com.terning.server.kotlin.domain.filter

import com.terning.server.kotlin.domain.common.BaseRootEntity
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
    private var jobType: JobType,
    @Enumerated(EnumType.STRING)
    private var grade: Grade,
    @Enumerated(EnumType.STRING)
    private var workingPeriod: WorkingPeriod,
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "year.value", column = Column(name = "start_year", nullable = false)),
        AttributeOverride(name = "month.value", column = Column(name = "start_month", nullable = false)),
    )
    private var startDate: StartDate,
) : BaseRootEntity() {
    companion object {
        fun of(
            jobType: JobType,
            grade: Grade,
            workingPeriod: WorkingPeriod,
            startDate: StartDate,
        ): Filter =
            Filter(
                jobType = jobType,
                grade = grade,
                workingPeriod = workingPeriod,
                startDate = startDate,
            )
    }

    fun update(
        newJobType: JobType,
        newGrade: Grade,
        newWorkingPeriod: WorkingPeriod,
        newStartDate: StartDate,
    ) {
        this.jobType = newJobType
        this.grade = newGrade
        this.workingPeriod = newWorkingPeriod
        this.startDate = newStartDate
    }

    fun jobType(): JobType = jobType

    fun grade(): Grade = grade

    fun workingPeriod(): WorkingPeriod = workingPeriod

    fun startDate(): StartDate = startDate

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Filter
        return id != null && id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}
