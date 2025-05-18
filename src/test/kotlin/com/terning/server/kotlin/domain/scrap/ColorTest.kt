package com.terning.server.kotlin.domain.scrap

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ColorTest {

    @Nested
    @DisplayName("from(color: String)")
    inner class From {

        @Test
        @DisplayName("정상적인 color 문자열을 전달하면 해당 Color를 반환한다")
        fun returnsColorWhenColorIsValid() {
            // given
            val input = "red"

            // when
            val color = Color.from(input)

            // then
            assertThat(color).isEqualTo(Color.RED)
        }

        @Test
        @DisplayName("존재하지 않는 color 문자열을 전달하면 ScrapException이 발생한다")
        fun throwsExceptionWhenColorIsInvalid() {
            // expect
            assertThatThrownBy { Color.from("not-a-color") }
                .isInstanceOf(ScrapException::class.java)
                .hasMessage(ScrapErrorCode.INVALID_COLOR.message)
        }
    }

    @Nested
    @DisplayName("toHexString()")
    inner class ToHexString {

        @Test
        @DisplayName("HEX 코드 앞에 #을 붙여 반환한다")
        fun returnsHexWithHashPrefix() {
            // given
            val color = Color.BLUE

            // when
            val hex = color.toHexString()

            // then
            assertThat(hex).isEqualTo("#4AA9F2")
        }
    }
}
