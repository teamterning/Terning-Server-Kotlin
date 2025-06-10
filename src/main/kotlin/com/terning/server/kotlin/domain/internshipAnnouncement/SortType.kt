package com.terning.server.kotlin.domain.internshipAnnouncement

import com.querydsl.core.types.OrderSpecifier
import com.terning.server.kotlin.domain.internshipAnnouncement.QInternshipAnnouncement.internshipAnnouncement
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException

enum class SortType(
    val description: String,
    val orderSpecifier: OrderSpecifier<*>,
) {
    DEADLINE_SOON("마감 임박순", internshipAnnouncement.internshipAnnouncementDeadline.value.asc()),
    SHORTEST_DURATION("짧은 기간순", internshipAnnouncement.workingPeriod.value.asc()),
    LONGEST_DURATION("긴 기간순", internshipAnnouncement.workingPeriod.value.desc()),
    MOST_SCRAPPED("스크랩 많은순", internshipAnnouncement.internshipAnnouncementScrapCount.value.desc()),
    MOST_VIEWED("조회수 많은순", internshipAnnouncement.internshipAnnouncementViewCount.value.desc()),
    ;

    companion object {
        fun from(raw: String): SortType =
            entries.find { it.name.equals(raw, ignoreCase = true) }
                ?: throw InternshipAnnouncementException(InternshipAnnouncementErrorCode.INVALID_SORT_TYPE)
    }
}
