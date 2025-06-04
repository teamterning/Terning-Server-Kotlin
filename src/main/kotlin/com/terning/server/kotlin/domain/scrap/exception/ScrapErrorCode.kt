package com.terning.server.kotlin.domain.scrap.exception

import com.terning.server.kotlin.domain.common.BaseErrorCode
import org.springframework.http.HttpStatus

enum class ScrapErrorCode(
    override val status: HttpStatus,
    override val message: String,
) : BaseErrorCode {
    INVALID_COLOR(HttpStatus.BAD_REQUEST, "유효하지 않은 스크랩 색상입니다."),
    INVALID_JOB_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 직무 유형입니다."),

    EXISTS_SCRAP_ALREADY(HttpStatus.BAD_REQUEST, "이미 스크랩한 공고입니다."),
    INTERN_SHIP_ANNOUNCEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 인턴 공고는 존재하지 않습니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저는 존재하지 않습니다"),

    SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스크랩은 존재하지 않습니다"),
}
