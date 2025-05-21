package com.terning.server.kotlin.domain.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus

class AuthTypeTest {
    @Test
    @DisplayName("유효하지 않은 토큰은 예외가 발생한다.")
    fun throwAuthExceptionWhenTokenIsInvalid() {
        // when
        val exception = assertThrows<AuthException> {
                throw AuthException(AuthErrorCode.INVALID_TOKEN)
            }

        // then
        assertThat(exception.message).isEqualTo("유효하지 않은 토큰입니다.")
        assertThat(exception.errorCode.status).isEqualTo(HttpStatus.UNAUTHORIZED)
    }
}
