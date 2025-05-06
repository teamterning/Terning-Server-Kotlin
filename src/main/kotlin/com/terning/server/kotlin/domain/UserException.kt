package com.terning.server.kotlin.domain

class UserException(
    val errorCode: UserErrorCode,
) : RuntimeException(errorCode.message)
