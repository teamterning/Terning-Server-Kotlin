package com.terning.server.kotlin.application.search.dto

import com.querydsl.core.Tuple
import com.terning.server.kotlin.domain.internshipAnnouncement.QInternshipAnnouncement.internshipAnnouncement
import com.terning.server.kotlin.domain.scrap.QScrap.scrap
import org.springframework.data.domain.Page
import java.time.Clock
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class SearchPageResponse(
    val totalPages: Int,
    val totalCount: Long,
    val hasNext: Boolean,
    val announcements: List<SearchAnnouncementResponse>,
) {
    companion object {
        fun from(page: Page<SearchAnnouncementResponse>): SearchPageResponse {
            return SearchPageResponse(
                totalPages = page.totalPages,
                totalCount = page.totalElements,
                hasNext = page.hasNext(),
                announcements = page.content,
            )
        }
    }
}

data class SearchAnnouncementResponse(
    val internshipAnnouncementId: Long,
    val companyImage: String,
    val dDay: String,
    val title: String,
    val workingPeriod: String,
    val isScrapped: Boolean,
    val color: String?,
    val deadline: String,
    val startYearMonth: String,
) {
    companion object {
        fun of(
            tuple: Tuple,
            clock: Clock,
        ): SearchAnnouncementResponse {
            val announcement = tuple.get(internshipAnnouncement)!!
            val scrapColor = tuple.get(scrap.color)

            return SearchAnnouncementResponse(
                internshipAnnouncementId = announcement.id!!,
                companyImage = announcement.company.logoUrl.value,
                dDay = calculateDday(announcement.internshipAnnouncementDeadline.value, clock),
                title = announcement.title.value,
                workingPeriod = announcement.workingPeriod.toString(),
                isScrapped = scrapColor != null,
                color = scrapColor?.toHexString(),
                deadline = formatDate(announcement.internshipAnnouncementDeadline.value),
                startYearMonth = "${announcement.startDate.year.value}년 ${announcement.startDate.month.value}월",
            )
        }

        private fun calculateDday(
            deadline: LocalDate?,
            clock: Clock,
        ): String {
            if (deadline == null) return "채용 시 마감"

            val daysLeft = ChronoUnit.DAYS.between(LocalDate.now(clock), deadline)
            return when {
                daysLeft < 0 -> "지원마감"
                daysLeft == 0L -> "D-day"
                else -> "D-$daysLeft"
            }
        }

        private fun formatDate(date: LocalDate?): String {
            return date?.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일")) ?: "채용 시 마감"
        }
    }
}
