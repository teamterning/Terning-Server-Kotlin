package com.terning.server.kotlin.domain.scrap

import org.springframework.http.HttpStatus

enum class ScrapErrorCode(
    val status: HttpStatus,
    val message: String,
) {
    INVALID_COLOR(HttpStatus.BAD_REQUEST, "유효하지 않은 스크랩 색상입니다."),
}
