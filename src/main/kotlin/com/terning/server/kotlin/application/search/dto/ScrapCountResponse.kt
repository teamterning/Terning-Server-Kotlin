package com.terning.server.kotlin.application.search.dto

import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncement

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
            return ScrapCountAnnouncementResponse(
                internshipAnnouncementId = announcement.id!!,
                companyImage = announcement.company.logoUrl.value,
                title = announcement.title.value,
            )
        }
    }
}
