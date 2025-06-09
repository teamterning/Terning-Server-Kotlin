package com.terning.server.kotlin.domain.filter.exception

import com.terning.server.kotlin.domain.common.BaseErrorCode
import org.springframework.http.HttpStatus

enum class FilterErrorCode(
    override val status: HttpStatus,
    override val message: String,
) : BaseErrorCode {
    NOT_FOUND_USER_EXCEPTION(HttpStatus.BAD_REQUEST, "해당 유저가 존재하지 않습니다"),
    INVALID_WORKING_PERIOD(HttpStatus.BAD_REQUEST, "유효하지 않은 근무 기간입니다."),
    INVALID_GRADE(HttpStatus.BAD_REQUEST, "유효하지 않은 학년입니다."),
    INVALID_YEAR(HttpStatus.BAD_REQUEST, "연도는 1900보다 커야 합니다."),
}
