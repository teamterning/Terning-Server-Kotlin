package com.terning.server.kotlin.domain.scrap

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ColorTest {
    @Nested
    @DisplayName("from(label: String)")
    inner class From {
        @Test
        @DisplayName("정상적인 label을 전달하면 해당 Color를 반환한다")
        fun returnsColorWhenLabelIsValid() {
            val color = Color.from("red")

            assertThat(color).isEqualTo(Color.RED)
        }

        @Test
        @DisplayName("존재하지 않는 label을 전달하면 ScrapException이 발생한다")
        fun throwsExceptionWhenLabelIsInvalid() {
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
            val color = Color.BLUE

            assertThat(color.toHexString()).isEqualTo("#4AA9F2")
        }
    }
}
