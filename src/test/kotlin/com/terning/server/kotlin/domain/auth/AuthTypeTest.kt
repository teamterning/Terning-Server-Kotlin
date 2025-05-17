package com.terning.server.kotlin.domain.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class AuthTypeTest {

    @Test
    @DisplayName("prefix와 message 결합하여 반환한다")
    fun getPrefixMessage() {
        val errorCode = AuthErrorCode.INVALID_TOKEN
        val expectedMessage = "[AUTH ERROR] 유효하지 않은 토큰입니다."

        assertThat(errorCode.getPrefixMessage()).isEqualTo(expectedMessage)
        assertThat(errorCode.status).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(errorCode.message).isEqualTo("유효하지 않은 토큰입니다.")
    }
}