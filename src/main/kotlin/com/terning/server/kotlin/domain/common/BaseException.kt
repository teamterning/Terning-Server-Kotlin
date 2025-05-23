package com.terning.server.kotlin.domain.common

abstract class BaseException(
    val errorCode: BaseErrorCode,
) : RuntimeException(errorCode.message)
