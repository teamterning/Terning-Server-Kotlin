package com.terning.server.kotlin.domain.auth.vo

import com.terning.server.kotlin.domain.auth.exception.AuthErrorCode
import com.terning.server.kotlin.domain.auth.exception.AuthException

enum class AuthType(val value: String) {
    KAKAO("KAKAO"),
    APPLE("APPLE"),
    ;

    companion object {
        fun from(value: String): AuthType =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw AuthException(AuthErrorCode.INVALID_AUTH_TYPE)
    }
}
