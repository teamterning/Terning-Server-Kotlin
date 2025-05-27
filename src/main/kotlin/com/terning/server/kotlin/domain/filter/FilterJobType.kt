package com.terning.server.kotlin.domain.filter

import com.terning.server.kotlin.domain.scrap.ScrapErrorCode
import com.terning.server.kotlin.domain.scrap.ScrapException

enum class FilterJobType(
    val type: String,
    val label: String,
) {
    TOTAL("total", "전체"),
    PLAN("plan", "기획/전략"),
    MARKETING("marketing", "마케팅/홍보"),
    ADMIN("admin", "사무/회계"),
    SALES("sales", "인사/영업"),
    DESIGN("design", "디자인/예술"),
    IT("it", "개발/IT"),
    RESEARCH("research", "연구/생산"),
    ETC("etc", "기타"),
    ;

    companion object {
        fun from(type: String): FilterJobType =
            entries.firstOrNull { it.type.equals(type, ignoreCase = true) }
                ?: throw ScrapException(ScrapErrorCode.INVALID_JOB_TYPE)
    }
}
