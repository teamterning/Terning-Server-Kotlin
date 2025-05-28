package com.terning.server.kotlin.domain.filter

import com.terning.server.kotlin.domain.filter.vo.FilterGrade
import com.terning.server.kotlin.domain.filter.vo.FilterJobType
import com.terning.server.kotlin.domain.filter.vo.FilterMonth
import com.terning.server.kotlin.domain.filter.vo.FilterStartDate
import com.terning.server.kotlin.domain.filter.vo.FilterWorkingPeriod
import com.terning.server.kotlin.domain.filter.vo.FilterYear
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class FilterTest {
    private val filterJobType = FilterJobType.IT
    private val filterGrade = FilterGrade.SENIOR
    private val filterWorkingPeriod = FilterWorkingPeriod.LONG_TERM
    private val filterStartDate =
        FilterStartDate.of(
            FilterYear.from(2025),
            FilterMonth.from(6),
        )

    private val newFilterJobType = FilterJobType.DESIGN
    private val newFilterGrade = FilterGrade.FRESHMAN
    private val newFilterWorkingPeriod = FilterWorkingPeriod.SHORT_TERM
    private val newFilterStartDate =
        FilterStartDate.of(
            FilterYear.from(2026),
            FilterMonth.from(1),
        )

    @Test
    @DisplayName("Filter를 생성하면 주어진 값이 저장된다")
    fun createFilter() {
        // when
        val filter = Filter.of(filterJobType, filterGrade, filterWorkingPeriod, filterStartDate)

        // then
        assertThat(filter.jobType()).isEqualTo(filterJobType)
        assertThat(filter.grade()).isEqualTo(filterGrade)
        assertThat(filter.workingPeriod()).isEqualTo(filterWorkingPeriod)
        assertThat(filter.startDate()).isEqualTo(filterStartDate)
    }

    @Test
    @DisplayName("Filter의 값을 update로 변경할 수 있다")
    fun updateFilter() {
        // given
        val filter = Filter.of(filterJobType, filterGrade, filterWorkingPeriod, filterStartDate)

        // when
        filter.update(newFilterJobType, newFilterGrade, newFilterWorkingPeriod, newFilterStartDate)

        // then
        assertThat(filter.jobType()).isEqualTo(newFilterJobType)
        assertThat(filter.grade()).isEqualTo(newFilterGrade)
        assertThat(filter.workingPeriod()).isEqualTo(newFilterWorkingPeriod)
        assertThat(filter.startDate()).isEqualTo(newFilterStartDate)
    }
}
