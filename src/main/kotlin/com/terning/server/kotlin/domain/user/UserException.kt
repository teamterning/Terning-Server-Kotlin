package com.terning.server.kotlin.domain.user

class UserException(
    val errorCode: UserErrorCode,
) : RuntimeException(errorCode.message)
