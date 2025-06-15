package com.terning.server.kotlin.application.home.dto

import com.querydsl.core.Tuple
import com.terning.server.kotlin.domain.internshipAnnouncement.QInternshipAnnouncement.internshipAnnouncement
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import com.terning.server.kotlin.domain.scrap.QScrap.scrap
import org.springframework.data.domain.Page

data class HomeResponse(
    val totalPages: Int,
    val totalCount: Long,
    val hasNext: Boolean,
    val announcements: List<Home>,
) {
    companion object {
        fun from(pagedTuples: Page<Tuple>): HomeResponse {
            val announcements = pagedTuples.content.map(Home.Companion::from)

            return HomeResponse(
                totalPages = pagedTuples.totalPages,
                totalCount = pagedTuples.totalElements,
                hasNext = pagedTuples.hasNext(),
                announcements = announcements,
            )
        }
    }
}

data class Home(
    val announcementId: Long,
    val companyImageUrl: String,
    val title: String,
    val workingPeriod: String,
    val isScrapped: Boolean,
    val hexColor: String?,
) {
    companion object {
        fun from(tuple: Tuple): Home {
            return tuple[internshipAnnouncement]?.let { announcement ->
                Home(
                    announcementId =
                        announcement.id
                            ?: throw InternshipAnnouncementException(InternshipAnnouncementErrorCode.NOT_FOUND_ANNOUNCEMENT_EXCEPTION),
                    companyImageUrl = announcement.company.logoUrl.value,
                    title = announcement.title.value,
                    workingPeriod = announcement.workingPeriod.toString(),
                    isScrapped = tuple[scrap.id] != null,
                    hexColor = tuple[scrap.color]?.toHexString(),
                )
            } ?: throw InternshipAnnouncementException(InternshipAnnouncementErrorCode.NOT_FOUND_ANNOUNCEMENT_EXCEPTION)
        }
    }
}
