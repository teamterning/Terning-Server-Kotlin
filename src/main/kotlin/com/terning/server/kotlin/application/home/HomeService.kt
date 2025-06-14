package com.terning.server.kotlin.application.home

import com.querydsl.core.Tuple
import com.terning.server.kotlin.application.home.dto.HomeResponse
import com.terning.server.kotlin.application.home.dto.UpcomingDeadlineScrapDetail
import com.terning.server.kotlin.application.home.dto.UpcomingDeadlineScrapResponse
import com.terning.server.kotlin.domain.filter.Filter
import com.terning.server.kotlin.domain.filter.FilterRepository
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncementRepository
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import com.terning.server.kotlin.domain.scrap.ScrapRepository
import com.terning.server.kotlin.domain.user.User
import com.terning.server.kotlin.domain.user.UserRepository
import com.terning.server.kotlin.domain.user.exception.UserErrorCode
import com.terning.server.kotlin.domain.user.exception.UserException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class HomeService(
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
    ): HomeResponse {
        val user =
            userRepository.findById(userId)
                .orElseThrow {
                    InternshipAnnouncementException(InternshipAnnouncementErrorCode.NOT_FOUND_USER_EXCEPTION)
                }

        val filter = filterRepository.findLatestByUser(user)
        val pagedTuples = fetchAnnouncements(user, filter, sortBy, pageable)

        if (pagedTuples.isEmpty) {
            return HomeResponse(
                totalPages = 0,
                totalCount = 0,
                hasNext = false,
                announcements = emptyList(),
            )
        }

        return HomeResponse.from(pagedTuples)
    }

    fun findUpcomingDeadlineScraps(userId: Long): UpcomingDeadlineScrapResponse {
        if (!userRepository.existsById(userId)) {
            throw UserException(UserErrorCode.USER_NOT_FOUND)
        }

        if (!scrapRepository.existsByUserId(userId)) {
            return UpcomingDeadlineScrapResponse(
                hasScrapped = false,
                message = "아직 스크랩된 인턴 공고가 없어요!",
                scraps = emptyList(),
            )
        }

        val today = LocalDate.now(clock)
        val sevenDaysLater = today.plusDays(7)

        val upcomingScraps =
            scrapRepository.findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(
                userId = userId,
                start = today,
                end = sevenDaysLater,
            )

        if (upcomingScraps.isEmpty()) {
            return UpcomingDeadlineScrapResponse(
                hasScrapped = true,
                message = "일주일 내에 마감인 공고가 없어요\n캘린더에서 스크랩한 공고 일정을 확인해 보세요",
                scraps = emptyList(),
            )
        }

        val scrapDetails =
            upcomingScraps.map { scrap ->
                UpcomingDeadlineScrapDetail.of(scrap, clock)
            }

        return UpcomingDeadlineScrapResponse(
            hasScrapped = true,
            message = "곧 마감되는 스크랩 공고를 성공적으로 조회했습니다.",
            scraps = scrapDetails,
        )
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
