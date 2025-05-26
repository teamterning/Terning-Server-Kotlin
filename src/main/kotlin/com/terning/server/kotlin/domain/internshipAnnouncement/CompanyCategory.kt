package com.terning.server.kotlin.domain.internshipAnnouncement

enum class CompanyCategory(
    val displayName: String,
) {
    LARGE_AND_MEDIUM_COMPANIES("대기업/중견기업"),
    SMALL_COMPANIES("중소기업"),
    PUBLIC_INSTITUTIONS("공공기관/공기업"),
    FOREIGN_COMPANIES("외국계기업"),
    STARTUPS("스타트업"),
    NON_PROFIT_ORGANIZATIONS("비영리단체/재단"),
    OTHERS("기타"),
    ;

    companion object {
        fun from(displayName: String): CompanyCategory =
            entries.firstOrNull { it.displayName == displayName }
                ?: throw InternshipException(InternshipErrorCode.INVALID_COMPANY_CATEGORY)
    }
}
