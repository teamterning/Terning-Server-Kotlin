package com.terning.server.kotlin.domain.filter.vo

import com.terning.server.kotlin.domain.filter.FilterErrorCode
import com.terning.server.kotlin.domain.filter.FilterException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class FilterWorkingPeriodTest {
    @Nested
    @DisplayName("from 메서드는")
    inner class From {
        @ParameterizedTest(name = "[{index}] period가 \"{0}\"이면 {1} 을(를) 반환한다")
        @CsvSource(
            "short, SHORT_TERM",
            "middle, MID_TERM",
            "long, LONG_TERM",
        )
        @DisplayName("유효한 period가 주어지면 해당 WorkingPeriod를 반환한다")
        fun validPeriodReturnsWorkingPeriod(
            period: String,
            expected: FilterWorkingPeriod,
        ) {
            // when
            val result = FilterWorkingPeriod.from(period)

            // then
            assertThat(result).isEqualTo(expected)
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "invalid", "Short", "LONGTERM", "mid"])
        @DisplayName("유효하지 않은 period가 주어지면 FilterException을 던진다")
        fun invalidPeriodThrowsException(invalidPeriod: String) {
            // expect
            assertThatThrownBy { FilterWorkingPeriod.from(invalidPeriod) }
                .isInstanceOfSatisfying(FilterException::class.java) { ex ->
                    assertThat(ex.errorCode).isEqualTo(FilterErrorCode.INVALID_WORKING_PERIOD)
                }
        }
    }
}
