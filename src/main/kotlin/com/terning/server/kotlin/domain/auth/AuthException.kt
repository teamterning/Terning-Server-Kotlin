package com.terning.server.kotlin.domain.auth

class AuthException(
    val errorCode: AuthErrorCode,
) : RuntimeException(errorCode.message)
