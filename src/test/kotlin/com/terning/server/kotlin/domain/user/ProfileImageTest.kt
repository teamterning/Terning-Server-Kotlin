package com.terning.server.kotlin.domain.user

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ProfileImageTest {
    @Nested
    @DisplayName("from(value) 메서드는")
    inner class FromMethod {
        @Test
        @DisplayName("문자열이 정확히 일치하면 해당 enum을 반환한다")
        fun returnsEnumWhenExactMatch() {
            val result = ProfileImage.from("basic")
            assertThat(result).isEqualTo(ProfileImage.BASIC)
        }

        @Test
        @DisplayName("문자열이 대소문자 구분 없이 일치하면 해당 enum을 반환한다")
        fun returnsEnumIgnoringCase() {
            val result = ProfileImage.from("SmArT")
            assertThat(result).isEqualTo(ProfileImage.SMART)
        }

        @Test
        @DisplayName("문자열이 일치하지 않으면 예외를 던진다")
        fun throwsExceptionOnInvalidValue() {
            assertThatThrownBy { ProfileImage.from("invalid") }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("유효하지 않은 프로필 이미지입니다: invalid")
        }
    }
}
