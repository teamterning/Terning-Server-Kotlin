package com.terning.server.kotlin.domain.auth.exception

import com.terning.server.kotlin.domain.common.BaseErrorCode
import org.springframework.http.HttpStatus

enum class AuthErrorCode(
    override val status: HttpStatus,
    override val message: String,
) : BaseErrorCode {
    NOT_FOUND_USER_EXCEPTION(
        status = HttpStatus.BAD_REQUEST,
        message = "해당 유저가 존재하지 않습니다",
    ),
    INVALID_TOKEN(
        status = HttpStatus.UNAUTHORIZED,
        message = "유효하지 않은 토큰입니다.",
    ),
    FAILED_REFRESH_TOKEN_RESET(
        status = HttpStatus.BAD_REQUEST,
        message = "리프레시 토큰 초기화에 실패하였습니다",
    ),
    FAILED_SOCIAL_LOGIN(
        status = HttpStatus.NOT_FOUND,
        message = "소셜 로그인에 실패하였습니다",
    ),
    INVALID_KEY(
        status = HttpStatus.UNAUTHORIZED,
        message = "유효하지 않은 키입니다",
    ),
    INVALID_AUTH_TYPE(
        status = HttpStatus.UNAUTHORIZED,
        message = "유효하지 않은 타입입니다",
    ),
}
