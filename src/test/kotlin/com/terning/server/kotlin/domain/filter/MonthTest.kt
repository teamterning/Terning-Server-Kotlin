package com.terning.server.kotlin.domain.filter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MonthTest {
    @Test
    @DisplayName("1부터 12 사이 값으로 Month를 생성할 수 있다")
    fun createValidMonth() {
        // given
        val value = 6

        // when
        val month = Month.from(value)

        // then
        assertThat(month.value).isEqualTo(6)
    }

    @Test
    @DisplayName("0 이하 또는 13 이상의 월은 예외가 발생한다")
    fun throwExceptionWhenMonthIsInvalid() {
        // expect
        assertThrows<IllegalArgumentException> {
            Month.from(0)
        }
        assertThrows<IllegalArgumentException> {
            Month.from(13)
        }
    }
}
