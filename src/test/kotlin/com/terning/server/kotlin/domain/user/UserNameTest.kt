package com.terning.server.kotlin.domain.user

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UserNameTest {
    @Nested
    @DisplayName("from(value)는")
    inner class From {
        @Test
        @DisplayName("유효한 이름이면 인스턴스를 반환한다")
        fun createWithValidName() {
            val name = UserName.from("장순님")
            assertThat(name.value).isEqualTo("장순님")
        }

        @Test
        @DisplayName("공백 이름이면 예외를 발생시킨다")
        fun throwsExceptionOnBlankName() {
            assertThatThrownBy { UserName.from("   ") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("이름은 공백일 수 없습니다.")
        }

        @Test
        @DisplayName("길이가 너무 짧으면 예외를 발생시킨다")
        fun throwsExceptionOnTooShortName() {
            assertThatThrownBy { UserName.from("") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("이름은 공백일 수 없습니다.")
        }

        @Test
        @DisplayName("길이가 너무 길면 예외를 발생시킨다")
        fun throwsExceptionOnTooLongName() {
            assertThatThrownBy { UserName.from("1234567890123") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("이름은 1~12자여야 합니다.")
        }
    }
}
