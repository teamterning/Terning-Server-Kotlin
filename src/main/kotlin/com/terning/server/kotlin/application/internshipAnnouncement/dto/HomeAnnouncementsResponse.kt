package com.terning.server.kotlin.application.internshipAnnouncement.dto

import com.querydsl.core.Tuple
import com.terning.server.kotlin.domain.internshipAnnouncement.QInternshipAnnouncement.internshipAnnouncement
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import com.terning.server.kotlin.domain.scrap.QScrap.scrap
import org.springframework.data.domain.Page

data class HomeAnnouncementsResponse(
    val totalPages: Int,
    val totalCount: Long,
    val hasNext: Boolean,
    val announcements: List<HomeAnnouncement>,
) {
    companion object {
        fun from(pagedTuples: Page<Tuple>): HomeAnnouncementsResponse {
            val announcements = pagedTuples.content.map(HomeAnnouncement::from)

            return HomeAnnouncementsResponse(
                totalPages = pagedTuples.totalPages,
                totalCount = pagedTuples.totalElements,
                hasNext = pagedTuples.hasNext(),
                announcements = announcements,
            )
        }
    }
}

data class HomeAnnouncement(
    val announcementId: Long,
    val companyImageUrl: String,
    val title: String,
    val workingPeriod: String,
    val isScrapped: Boolean,
    val hexColor: String?,
) {
    companion object {
        fun from(tuple: Tuple): HomeAnnouncement {
            val announcement =
                tuple[internshipAnnouncement]
                    ?: throw InternshipAnnouncementException(InternshipAnnouncementErrorCode.NOT_FOUND_ANNOUNCEMENT_EXCEPTION)

            val announcementId =
                announcement.id
                    ?: throw InternshipAnnouncementException(InternshipAnnouncementErrorCode.NOT_FOUND_ANNOUNCEMENT_EXCEPTION)

            return HomeAnnouncement(
                announcementId = announcementId,
                companyImageUrl = announcement.company.logoUrl.value,
                title = announcement.title.value,
                workingPeriod = announcement.workingPeriod.toString(),
                isScrapped = tuple[scrap.id] != null,
                hexColor = tuple[scrap.color]?.toHexString(),
            )
        }
    }
}
