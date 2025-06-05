package com.terning.server.kotlin.domain.user.exception

import com.terning.server.kotlin.domain.common.BaseErrorCode
import org.springframework.http.HttpStatus

enum class UserErrorCode(
    override val status: HttpStatus,
    override val message: String,
) : BaseErrorCode {
    NOT_FOUND_USER_EXCEPTION(HttpStatus.BAD_REQUEST, "해당 유저가 존재하지 않습니다"),
    INVALID_PROFILE_IMAGE(HttpStatus.BAD_REQUEST, "유효하지 않은 프로필 이미지 입니다."),
}
