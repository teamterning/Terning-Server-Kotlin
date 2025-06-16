package com.terning.server.kotlin.application.announcement

import com.terning.server.kotlin.application.announcement.dto.DetailAnnouncementResponse
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncementRepository
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementUrl
import com.terning.server.kotlin.domain.scrap.ScrapRepository
import com.terning.server.kotlin.domain.scrap.exception.ScrapErrorCode
import com.terning.server.kotlin.domain.scrap.exception.ScrapException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock

@Service
@Transactional(readOnly = true)
class AnnouncementService(
    private val internshipRepository: InternshipAnnouncementRepository,
    private val scrapRepository: ScrapRepository,
    private val clock: Clock,
) {
    fun getDetailAnnouncement(
        userId: Long,
        internshipAnnouncementId: Long,
    ): DetailAnnouncementResponse {
        val announcement =
            internshipRepository.findById(internshipAnnouncementId).orElseThrow {
                ScrapException(ScrapErrorCode.INTERN_SHIP_ANNOUNCEMENT_NOT_FOUND)
            }

        announcement.increaseViewCount()

        val scrap =
            scrapRepository.findByUserIdAndInternshipAnnouncementId(
                userId = userId,
                internshipAnnouncementId = internshipAnnouncementId,
            )

        val (isScrapped, color) =
            if (scrap != null) {
                true to scrap.hexColor()
            } else {
                false to null
            }

        return DetailAnnouncementResponse.from(
            companyImage = announcement.company.logoUrl.value,
            deadline = announcement.internshipAnnouncementDeadline.value,
            clock = clock,
            title = announcement.title.value,
            workingPeriod = announcement.workingPeriod.value,
            isScrapped = isScrapped,
            color = color,
            startYear = announcement.startDate.year.value,
            startMonth = announcement.startDate.month.value,
            scrapCount = announcement.internshipAnnouncementScrapCount.value,
            viewCount = announcement.internshipAnnouncementViewCount.value,
            company = announcement.company.name.value,
            companyCategory = announcement.company.category.displayName,
            qualification = announcement.qualifications.orEmpty(),
            jobType = announcement.filterJobType.type,
            detail = announcement.detail.orEmpty(),
            url = InternshipAnnouncementUrl.from(announcement.url.value).toString(),
        )
    }
}
