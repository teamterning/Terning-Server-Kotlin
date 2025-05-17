package com.terning.server.kotlin.domain.scrap

import com.terning.server.kotlin.domain.common.BaseErrorCode
import org.springframework.http.HttpStatus

enum class ScrapErrorCode(
    override val status: HttpStatus,
    override val message: String,
) : BaseErrorCode {
    INVALID_COLOR(HttpStatus.BAD_REQUEST, "유효하지 않은 스크랩 색상입니다."),
}
