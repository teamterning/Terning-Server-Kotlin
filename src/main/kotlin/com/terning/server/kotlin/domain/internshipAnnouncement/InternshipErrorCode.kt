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
    INVALID_COMPANY_CATEGORY(HttpStatus.BAD_REQUEST, "올바르지 않은 기업 구분 값입니다."),
    INVALID_COMPANY_NAME_EMPTY(HttpStatus.BAD_REQUEST, "기업명은 비어 있을 수 없습니다."),
    INVALID_COMPANY_NAME_TOO_LONG(HttpStatus.BAD_REQUEST, "기업명은 64자 이하여야 합니다."),
    INVALID_INTERNSHIP_TITLE_EMPTY(HttpStatus.BAD_REQUEST, "인턴십 제목은 비어 있을 수 없습니다."),
    INVALID_INTERNSHIP_TITLE_TOO_LONG(HttpStatus.BAD_REQUEST, "인턴십 제목은 64자 이하여야 합니다."),
    INVALID_WORKING_PERIOD(HttpStatus.BAD_REQUEST, "근무 기간은 1개월 이상이어야 합니다."),
    INVALID_MONTH(HttpStatus.BAD_REQUEST, "월은 1~12 사이여야 합니다."),
    INVALID_YEAR(HttpStatus.BAD_REQUEST, "연도는 2025보다 커야 합니다."),
    INVALID_COMPANY_LOGO_URL(HttpStatus.BAD_REQUEST, "올바른 이미지 URL이 아닙니다."),
}
