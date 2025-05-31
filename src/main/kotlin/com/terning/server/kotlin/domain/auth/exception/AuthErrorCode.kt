package com.terning.server.kotlin.domain.auth.exception

import com.terning.server.kotlin.domain.common.BaseErrorCode
import org.springframework.http.HttpStatus

enum class AuthErrorCode(
    override val status: HttpStatus,
    override val message: String,
) : BaseErrorCode {
    INVALID_TOKEN(status = HttpStatus.UNAUTHORIZED, message = "유효하지 않은 토큰입니다."),
    FAILED_REFRESH_TOKEN_RESET(status = HttpStatus.BAD_REQUEST, message = "리프레시 토큰 초기화에 실패하였습니다"),
    ;

    fun getErrorMessage(): String = message
}
