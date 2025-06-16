package com.terning.server.kotlin.application.search.dto

import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncement
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException

data class ScrapCountResponse(
    val announcements: List<ScrapCountAnnouncementResponse>,
)

data class ScrapCountAnnouncementResponse(
    val internshipAnnouncementId: Long,
    val companyImage: String,
    val title: String,
) {
    companion object {
        fun from(announcement: InternshipAnnouncement): ScrapCountAnnouncementResponse {
            val announcementId =
                announcement.id
                    ?: throw InternshipAnnouncementException(InternshipAnnouncementErrorCode.ANNOUNCEMENT_ID_NULL)
            return ScrapCountAnnouncementResponse(
                internshipAnnouncementId = announcementId,
                companyImage = announcement.company.logoUrl.value,
                title = announcement.title.value,
            )
        }
    }
}
