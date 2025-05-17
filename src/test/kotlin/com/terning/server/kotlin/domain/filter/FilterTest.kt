package com.terning.server.kotlin.domain.filter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class FilterTest {
    private val jobType = JobType.IT
    private val grade = Grade.SENIOR
    private val workingPeriod = WorkingPeriod.LONG_TERM
    private val startDate =
        StartDate.of(
            Year.from(2025),
            Month.from(6),
        )

    private val newJobType = JobType.DESIGN
    private val newGrade = Grade.FRESHMAN
    private val newWorkingPeriod = WorkingPeriod.SHORT_TERM
    private val newStartDate =
        StartDate.of(
            Year.from(2026),
            Month.from(1),
        )

    @Test
    @DisplayName("Filter를 생성하면 주어진 값이 저장된다")
    fun createFilter() {
        // when
        val filter = Filter.of(jobType, grade, workingPeriod, startDate)

        // then
        assertThat(filter.jobType()).isEqualTo(jobType)
        assertThat(filter.grade()).isEqualTo(grade)
        assertThat(filter.workingPeriod()).isEqualTo(workingPeriod)
        assertThat(filter.startDate()).isEqualTo(startDate)
    }

    @Test
    @DisplayName("Filter의 값을 update로 변경할 수 있다")
    fun updateFilter() {
        // given
        val filter = Filter.of(jobType, grade, workingPeriod, startDate)

        // when
        filter.update(newJobType, newGrade, newWorkingPeriod, newStartDate)

        // then
        assertThat(filter.jobType()).isEqualTo(newJobType)
        assertThat(filter.grade()).isEqualTo(newGrade)
        assertThat(filter.workingPeriod()).isEqualTo(newWorkingPeriod)
        assertThat(filter.startDate()).isEqualTo(newStartDate)
    }
}
