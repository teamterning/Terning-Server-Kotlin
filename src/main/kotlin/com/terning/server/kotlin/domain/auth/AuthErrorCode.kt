package com.terning.server.kotlin.domain.auth

import org.springframework.http.HttpStatus

enum class AuthErrorCode(
    val status: HttpStatus,
    val message: String,
) {
    INVALID_TOKEN(status = HttpStatus.UNAUTHORIZED, message = "유효하지 않은 토큰입니다."),
    FAILED_REFRESH_TOKEN_RESET(status = HttpStatus.BAD_REQUEST, message = "리프레쉬 토큰 초기화에 실패하였습니다"),
    ;

    fun getErrorMessage(): String = message
}
