package com.terning.server.kotlin.application.internshipAnnouncement

import com.querydsl.core.Tuple
import com.terning.server.kotlin.application.internshipAnnouncement.dto.DetailAnnouncementResponse
import com.terning.server.kotlin.application.internshipAnnouncement.dto.HomeAnnouncementsResponse
import com.terning.server.kotlin.domain.filter.Filter
import com.terning.server.kotlin.domain.filter.FilterRepository
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncementRepository
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementUrl
import com.terning.server.kotlin.domain.scrap.ScrapRepository
import com.terning.server.kotlin.domain.scrap.exception.ScrapErrorCode
import com.terning.server.kotlin.domain.scrap.exception.ScrapException
import com.terning.server.kotlin.domain.user.User
import com.terning.server.kotlin.domain.user.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock

@Service
@Transactional(readOnly = true)
class InternshipAnnouncementService(
    private val internshipRepository: InternshipAnnouncementRepository,
    private val userRepository: UserRepository,
    private val filterRepository: FilterRepository,
    private val scrapRepository: ScrapRepository,
    private val clock: Clock,
) {
    fun getFilteredAnnouncements(
        userId: Long,
        sortBy: String,
        pageable: Pageable,
    ): HomeAnnouncementsResponse {
        val user =
            userRepository.findById(userId)
                .orElseThrow {
                    InternshipAnnouncementException(InternshipAnnouncementErrorCode.NOT_FOUND_USER_EXCEPTION)
                }

        val filter = filterRepository.findLatestByUser(user)
        val pagedTuples = fetchAnnouncements(user, filter, sortBy, pageable)

        if (pagedTuples.isEmpty) {
            return HomeAnnouncementsResponse(
                totalPages = 0,
                totalCount = 0,
                hasNext = false,
                announcements = emptyList(),
            )
        }

        return HomeAnnouncementsResponse.from(pagedTuples)
    }

    private fun fetchAnnouncements(
        user: User,
        filter: Filter?,
        sortBy: String,
        pageable: Pageable,
    ): Page<Tuple> {
        if (filter == null || filter.isDefault()) {
            return internshipRepository.findAllInternshipsWithScrapInfo(user, sortBy, pageable)
        }
        return internshipRepository.findFilteredInternshipsWithScrapInfo(user, filter, sortBy, pageable)
    }

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
