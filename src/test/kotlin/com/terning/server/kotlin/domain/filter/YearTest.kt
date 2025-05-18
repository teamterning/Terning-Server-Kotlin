package com.terning.server.kotlin.domain.filter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class YearTest {

    @Test
    @DisplayName("유효한 연도 값으로 Year를 생성할 수 있다")
    fun createValidYear() {
        // given
        val value = 2025

        // when
        val year = Year.from(value)

        // then
        assertThat(year.value).isEqualTo(2025)
    }

    @Test
    @DisplayName("1900년 이하의 연도는 예외가 발생한다")
    fun throwExceptionWhenYearIsTooSmall() {
        // expect
        assertThrows<IllegalArgumentException> {
            Year.from(1899)
        }
    }
}
