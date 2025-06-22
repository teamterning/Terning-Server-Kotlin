package com.terning.server.kotlin.domain.common.security.jwt.exception

import org.springframework.http.HttpStatus

enum class JwtErrorCode(
    val status: HttpStatus,
    private val rawMessage: String,
) {
    INVALID_USER_DETAILS_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 UserDetail 타입입니다."),
    ;

    fun message(): String = "$PREFIX $rawMessage"

    companion object {
        private const val PREFIX = "[JWT ERROR]"
    }
}
