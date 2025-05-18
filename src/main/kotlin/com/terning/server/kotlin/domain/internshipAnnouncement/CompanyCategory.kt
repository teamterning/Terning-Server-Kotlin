package com.terning.server.kotlin.domain.internshipAnnouncement

enum class CompanyCategory(
    val categoryId: Int,
    val description: String,
) {
    LARGE_AND_MEDIUM_COMPANIES(0, "대기업/중견기업"),
    SMALL_COMPANIES(1, "중소기업"),
    PUBLIC_INSTITUTIONS(2, "공공기관/공기업"),
    FOREIGN_COMPANIES(3, "외국계기업"),
    STARTUPS(4, "스타트업"),
    NON_PROFIT_ORGANIZATIONS(5, "비영리단체/재단"),
    OTHERS(6, "기타"),
    ;

    companion object {
        fun from(value: Int): CompanyCategory =
            entries.firstOrNull { it.categoryId == value }
                ?: throw InternshipException(InternshipErrorCode.INVALID_COMPANY_CATEGORY)
    }
}
