package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InternshipFilterWorkingPeriodTest {
    @Nested
    @DisplayName("InternshipWorkingPeriod.from 메서드는")
    inner class From {
        @Test
        @DisplayName("유효한 개월 수를 입력하면 객체를 생성한다")
        fun `create instance when valid months given`() {
            // given
            val months = 3

            // when
            val period = InternshipWorkingPeriod.from(months)

            // then
            assertThat(period.months).isEqualTo(months)
        }

        @Test
        @DisplayName("0 이하의 개월 수를 입력하면 예외를 던진다")
        fun `throw exception when months is zero or negative`() {
            val exception =
                assertThrows<InternshipException> {
                    InternshipWorkingPeriod.from(0)
                }

            assertThat(exception.errorCode).isEqualTo(InternshipErrorCode.INVALID_WORKING_PERIOD)
        }
    }

    @Nested
    @DisplayName("toKoreanPeriod 메서드는")
    inner class ToKoreanPeriod {
        @Test
        @DisplayName("개월 수를 'N개월' 형태의 문자열로 반환한다")
        fun `return correct korean period string`() {
            // given
            val period = InternshipWorkingPeriod.from(6)

            // when
            val result = period.toKoreanPeriod()

            // then
            assertThat(result).isEqualTo("6개월")
        }
    }
}
