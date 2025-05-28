package com.terning.server.kotlin.domain.filter.vo

import com.terning.server.kotlin.domain.filter.FilterErrorCode
import com.terning.server.kotlin.domain.filter.FilterException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FilterYearTest {
    @Test
    @DisplayName("유효한 연도 값으로 Year를 생성할 수 있다")
    fun createValidYear() {
        // given
        val value = 2025

        // when
        val filterYear = FilterYear.from(value)

        // then
        assertThat(filterYear.value).isEqualTo(2025)
    }

    @Test
    @DisplayName("1900년 이하의 연도는 예외가 발생한다")
    fun throwExceptionWhenYearIsTooSmall() {
        // expect
        val exception =
            assertThrows<FilterException> {
                FilterYear.from(1899)
            }
        assertThat(exception.errorCode).isEqualTo(FilterErrorCode.INVALID_YEAR)
    }
}
