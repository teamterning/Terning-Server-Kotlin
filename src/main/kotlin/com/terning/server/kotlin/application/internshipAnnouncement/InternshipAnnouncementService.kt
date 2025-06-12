package com.terning.server.kotlin.application.internshipAnnouncement

import com.querydsl.core.Tuple
import com.terning.server.kotlin.application.internshipAnnouncement.dto.HomeAnnouncementsResponse
import com.terning.server.kotlin.domain.filter.Filter
import com.terning.server.kotlin.domain.filter.FilterRepository
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncementRepository
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import com.terning.server.kotlin.domain.user.User
import com.terning.server.kotlin.domain.user.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class InternshipAnnouncementService(
    private val internshipRepository: InternshipAnnouncementRepository,
    private val userRepository: UserRepository,
    private val filterRepository: FilterRepository,
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
}
