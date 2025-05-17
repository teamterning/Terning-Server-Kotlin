package com.terning.server.kotlin.domain.common

import org.springframework.http.HttpStatus

interface BaseErrorCode {
    val status: HttpStatus
    val message: String
}
