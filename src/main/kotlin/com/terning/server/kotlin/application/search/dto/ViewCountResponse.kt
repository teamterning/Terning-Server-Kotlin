package com.terning.server.kotlin.application.search.dto

import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncement

data class ViewCountResponse(
    val announcements: List<ViewCountAnnouncementResponse>,
)

data class ViewCountAnnouncementResponse(
    val internshipAnnouncementId: Long,
    val companyImage: String,
    val title: String,
) {
    companion object {
        fun from(announcement: InternshipAnnouncement): ViewCountAnnouncementResponse {
            return ViewCountAnnouncementResponse(
                internshipAnnouncementId = announcement.id!!,
                companyImage = announcement.company.logoUrl.value,
                title = announcement.title.value,
            )
        }
    }
}
