package com.terning.server.kotlin.domain.internshipAnnouncement

import com.terning.server.kotlin.domain.common.BaseErrorCode
import org.springframework.http.HttpStatus

enum class InternshipErrorCode(
    override val status: HttpStatus,
    override val message: String,
) : BaseErrorCode {
    INVALID_DEADLINE(HttpStatus.BAD_REQUEST, "마감일은 2025년 이후여야 합니다."),
    INVALID_SCRAP_COUNT(HttpStatus.BAD_REQUEST, "스크랩 수는 음수일 수 없습니다."),
    SCRAP_COUNT_CANNOT_BE_DECREASED_BELOW_ZERO(HttpStatus.BAD_REQUEST, "스크랩 수는 0보다 작아질 수 없습니다."),
    INVALID_VIEW_COUNT(HttpStatus.BAD_REQUEST, "조회수는 음수일 수 없습니다."),
}
