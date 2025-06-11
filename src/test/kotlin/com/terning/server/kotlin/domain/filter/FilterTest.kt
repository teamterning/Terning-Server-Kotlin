package com.terning.server.kotlin.domain.filter

import com.terning.server.kotlin.domain.filter.vo.FilterGrade
import com.terning.server.kotlin.domain.filter.vo.FilterJobType
import com.terning.server.kotlin.domain.filter.vo.FilterMonth
import com.terning.server.kotlin.domain.filter.vo.FilterStartDate
import com.terning.server.kotlin.domain.filter.vo.FilterWorkingPeriod
import com.terning.server.kotlin.domain.filter.vo.FilterYear
import com.terning.server.kotlin.domain.user.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class FilterTest {
    private val user = User.of("테스터", "basic")

    private val filterJobType = FilterJobType.IT
    private val filterGrade = FilterGrade.SENIOR
    private val filterWorkingPeriod = FilterWorkingPeriod.LONG_TERM
    private val filterStartDate = FilterStartDate.of(FilterYear.from(2025), FilterMonth.from(6))

    private val newFilterJobType = FilterJobType.DESIGN
    private val newFilterGrade = FilterGrade.FRESHMAN
    private val newFilterWorkingPeriod = FilterWorkingPeriod.SHORT_TERM
    private val newFilterStartDate = FilterStartDate.of(FilterYear.from(2026), FilterMonth.from(1))

    @Test
    @DisplayName("Filter를 생성하면 주어진 값이 저장된다")
    fun createFilter() {
        val filter = Filter.of(user, filterJobType, filterGrade, filterWorkingPeriod, filterStartDate)

        assertThat(filter.jobType()).isEqualTo(filterJobType)
        assertThat(filter.grade()).isEqualTo(filterGrade)
        assertThat(filter.workingPeriod()).isEqualTo(filterWorkingPeriod)
        assertThat(filter.startDate()).isEqualTo(filterStartDate)
    }

    @Test
    @DisplayName("Filter의 값을 update로 변경할 수 있다")
    fun updateFilter() {
        val filter = Filter.of(user, filterJobType, filterGrade, filterWorkingPeriod, filterStartDate)

        filter.updateFilter(newFilterJobType, newFilterGrade, newFilterWorkingPeriod, newFilterStartDate)

        assertThat(filter.jobType()).isEqualTo(newFilterJobType)
        assertThat(filter.grade()).isEqualTo(newFilterGrade)
        assertThat(filter.workingPeriod()).isEqualTo(newFilterWorkingPeriod)
        assertThat(filter.startDate()).isEqualTo(newFilterStartDate)
    }

    @Nested
    @DisplayName("isDefault 메서드는")
    inner class IsDefaultTest {
        @Test
        @DisplayName("기본값을 가지는 경우 true를 반환한다")
        fun returnsTrueWhenFilterIsDefault() {
            val defaultFilter =
                Filter.of(
                    user = user,
                    filterJobType = FilterJobType.TOTAL,
                    filterGrade = FilterGrade.NONE,
                    filterWorkingPeriod = FilterWorkingPeriod.NONE,
                    filterStartDate =
                        FilterStartDate.of(
                            FilterYear.DEFAULT,
                            FilterMonth.DEFAULT,
                        ),
                )

            assertThat(defaultFilter.isDefault()).isTrue
        }

        @Test
        @DisplayName("기본값이 아닌 경우 false를 반환한다")
        fun returnsFalseWhenFilterIsNotDefault() {
            val nonDefaultFilter =
                Filter.of(
                    user = user,
                    filterJobType = FilterJobType.IT,
                    filterGrade = FilterGrade.SENIOR,
                    filterWorkingPeriod = FilterWorkingPeriod.LONG_TERM,
                    filterStartDate =
                        FilterStartDate.of(
                            FilterYear.from(2024),
                            FilterMonth.from(5),
                        ),
                )

            assertThat(nonDefaultFilter.isDefault()).isFalse
        }
    }
}
