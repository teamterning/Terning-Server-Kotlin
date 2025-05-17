package com.terning.server.kotlin.domain.auth

import org.springframework.http.HttpStatus

enum class AuthErrorCode(
    val status: HttpStatus,
    val message: String,
) {
    INVALID_TOKEN(status = HttpStatus.UNAUTHORIZED, message = "유효하지 않은 토큰입니다."),
    ;

    fun getMessage(): String = "$PREFIX $message"

    companion object {
        private const val PREFIX = "[AUTH ERROR]"
    }
}
