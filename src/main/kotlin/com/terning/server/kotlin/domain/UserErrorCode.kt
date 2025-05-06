package com.terning.server.kotlin.domain

import org.springframework.http.HttpStatus

enum class UserErrorCode(
    val status: HttpStatus,
    val message: String,
) {
    INVALID_PROFILE_IMAGE(HttpStatus.BAD_REQUEST, "유효하지 않은 프로필 이미지 입니다."),
}
