package com.terning.server.kotlin.application.search

import com.terning.server.kotlin.application.search.dto.SearchAnnouncementResponse
import com.terning.server.kotlin.application.search.dto.SearchPageResponse
import com.terning.server.kotlin.application.search.dto.ViewCountAnnouncementResponse
import com.terning.server.kotlin.application.search.dto.ViewCountResponse
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncementRepository
import com.terning.server.kotlin.domain.user.UserRepository
import com.terning.server.kotlin.domain.user.exception.UserErrorCode
import com.terning.server.kotlin.domain.user.exception.UserException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class SearchService(
    private val internshipAnnouncementRepository: InternshipAnnouncementRepository,
    private val userRepository: UserRepository,
    private val clock: Clock,
) {
    fun search(
        userId: Long,
        keyword: String?,
        sortBy: String,
        pageable: Pageable,
    ): SearchPageResponse {
        val user =
            userRepository.findById(userId)
                .orElseThrow { UserException(UserErrorCode.USER_NOT_FOUND) }

        val currentDate = LocalDate.now(clock)

        val announcementTuples =
            internshipAnnouncementRepository.findByKeywordWithScrapInfo(
                user = user,
                keyword = keyword,
                sortBy = sortBy,
                pageable = pageable,
                now = currentDate,
            )

        val announcementResponse = announcementTuples.map { tuple -> SearchAnnouncementResponse.of(tuple, clock) }

        return SearchPageResponse.from(announcementResponse)
    }

    fun getMostViewedAnnouncements(userId: Long): ViewCountResponse {
        if (!userRepository.existsById(userId)) {
            throw UserException(UserErrorCode.USER_NOT_FOUND)
        }

        val currentDate = LocalDate.now(clock)
        val announcements = internshipAnnouncementRepository.findTop5ByViews(currentDate)

        val responses =
            announcements.map {
                ViewCountAnnouncementResponse.from(it)
            }

        return ViewCountResponse(announcements = responses)
    }
}
