package com.terning.server.kotlin.domain.scrap

class ScrapException(
    val errorCode: ScrapErrorCode,
) : RuntimeException(errorCode.message)
