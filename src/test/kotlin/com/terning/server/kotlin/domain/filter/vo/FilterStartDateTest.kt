package com.terning.server.kotlin.domain.filter.vo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class FilterStartDateTest {
    @Test
    @DisplayName("Year와 Month로 StartDate를 생성할 수 있다")
    fun createStartDateWithYearAndMonth() {
        // given
        val filterYear = FilterYear.from(2023)
        val filterMonth = FilterMonth.from(5)

        // when
        val filterStartDate = FilterStartDate.of(filterYear, filterMonth)

        // then
        assertThat(filterStartDate.filterYear.value).isEqualTo(2023)
        assertThat(filterStartDate.filterMonth.value).isEqualTo(5)
    }
}
