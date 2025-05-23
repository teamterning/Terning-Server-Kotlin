package com.terning.server.kotlin.domain.filter

import com.terning.server.kotlin.domain.common.BaseErrorCode
import org.springframework.http.HttpStatus

enum class FilterErrorCode(
    override val status: HttpStatus,
    override val message: String,
) : BaseErrorCode {
    INVALID_WORKING_PERIOD(HttpStatus.BAD_REQUEST, "유효하지 않은 근무 기간입니다."),
    INVALID_GRADE(HttpStatus.BAD_REQUEST, "유효하지 않은 학년입니다."),
}
