package com.terning.server.kotlin.domain.filter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class StartDateTest {
    @Test
    @DisplayName("Year와 Month로 StartDate를 생성할 수 있다")
    fun createStartDateWithYearAndMonth() {
        // given
        val year = Year.from(2023)
        val month = Month.from(5)

        // when
        val startDate = StartDate.of(year, month)

        // then
        assertThat(startDate.year.value).isEqualTo(2023)
        assertThat(startDate.month.value).isEqualTo(5)
    }
}
