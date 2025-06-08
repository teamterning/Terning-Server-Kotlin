package com.terning.server.kotlin.application

import com.terning.server.kotlin.application.scrap.dto.DetailedMonthlyScrapResponse
import com.terning.server.kotlin.application.scrap.dto.DetailedScrap
import com.terning.server.kotlin.application.scrap.dto.DetailedScrapGroup
import com.terning.server.kotlin.application.scrap.dto.MonthlyScrapDeadlineGroup
import com.terning.server.kotlin.application.scrap.dto.MonthlyScrapDeadlineResponse
import com.terning.server.kotlin.application.scrap.dto.MonthlyScrapDeadlineSummary
import com.terning.server.kotlin.application.scrap.dto.ScrapRequest
import com.terning.server.kotlin.application.scrap.dto.ScrapUpdateRequest
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncementRepository
import com.terning.server.kotlin.domain.scrap.Scrap
import com.terning.server.kotlin.domain.scrap.ScrapRepository
import com.terning.server.kotlin.domain.scrap.exception.ScrapErrorCode
import com.terning.server.kotlin.domain.scrap.exception.ScrapException
import com.terning.server.kotlin.domain.scrap.vo.Color
import com.terning.server.kotlin.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class ScrapService(
    private val scrapRepository: ScrapRepository,
    private val userRepository: UserRepository,
    private val internshipAnnouncementRepository: InternshipAnnouncementRepository,
) {
    @Transactional
    fun scrap(
        userId: Long,
        internshipAnnouncementId: Long,
        scrapRequest: ScrapRequest,
    ) {
        if (scrapRepository.existsByUserIdAndInternshipAnnouncementId(userId, internshipAnnouncementId)) {
            throw ScrapException(ScrapErrorCode.EXISTS_SCRAP_ALREADY)
        }

        val announcement =
            internshipAnnouncementRepository.findById(internshipAnnouncementId)
                .orElseThrow { ScrapException(ScrapErrorCode.INTERN_SHIP_ANNOUNCEMENT_NOT_FOUND) }

        val user =
            userRepository.findById(userId)
                .orElseThrow { ScrapException(ScrapErrorCode.USER_NOT_FOUND) }

        val color = Color.from(scrapRequest.color)
        val scrap =
            Scrap.of(
                user = user,
                internshipAnnouncement = announcement,
                color = color,
            )

        scrapRepository.save(scrap)
        announcement.increaseScrapCount()
    }

    @Transactional
    fun updateScrap(
        userId: Long,
        internshipAnnouncementId: Long,
        scrapUpdateRequest: ScrapUpdateRequest,
    ) {
        val scrap =
            scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, internshipAnnouncementId)
                ?: throw ScrapException(ScrapErrorCode.SCRAP_NOT_FOUND)

        val color = Color.from(scrapUpdateRequest.color)
        scrap.updateColor(color)

        scrapRepository.save(scrap)
    }

    @Transactional
    fun cancelScrap(
        userId: Long,
        internshipAnnouncementId: Long,
    ) {
        val scrap =
            scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, internshipAnnouncementId)
                ?: throw ScrapException(ScrapErrorCode.SCRAP_NOT_FOUND)

        val announcement =
            internshipAnnouncementRepository.findById(internshipAnnouncementId)
                .orElseThrow { ScrapException(ScrapErrorCode.INTERN_SHIP_ANNOUNCEMENT_NOT_FOUND) }

        announcement.decreaseScrapCount()
        scrapRepository.delete(scrap)
    }

    fun monthlyScrapDeadlines(
        userId: Long,
        year: Int,
        month: Int,
    ): MonthlyScrapDeadlineResponse {
        val startDate = LocalDate.of(year, month, 1)
        val endDate = startDate.plusMonths(1).minusDays(1)

        val scraps =
            scrapRepository.findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(
                userId = userId,
                start = startDate,
                end = endDate,
            )

        val groupedByDeadline = scraps.groupBy { it.internshipAnnouncement.internshipAnnouncementDeadline.value }

        val monthlyGroups =
            groupedByDeadline.map { (deadline, groupedScraps) ->
                MonthlyScrapDeadlineGroup(
                    deadline = deadline.toString(),
                    scraps =
                        groupedScraps.map { scrap ->
                            MonthlyScrapDeadlineSummary(
                                scrapId = scrap.id ?: throw ScrapException(ScrapErrorCode.SCRAP_ID_NULL),
                                title = scrap.internshipAnnouncement.title.value,
                                color = scrap.hexColor(),
                            )
                        },
                )
            }
        return MonthlyScrapDeadlineResponse(monthlyScrapDeadline = monthlyGroups)
    }

    fun detailedMonthlyScraps(
        userId: Long,
        year: Int,
        month: Int,
    ): DetailedMonthlyScrapResponse {
        val startDate = LocalDate.of(year, month, 1)
        val endDate = startDate.plusMonths(1).minusDays(1)

        val scraps =
            scrapRepository.findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(
                userId = userId,
                start = startDate,
                end = endDate,
            )

        val dailyGroups =
            scraps
                .groupBy { it.internshipAnnouncement.internshipAnnouncementDeadline.value }
                .toSortedMap()
                .map { (deadline, dailyScraps) ->
                    DetailedScrapGroup(
                        deadline = deadline.toString(),
                        scraps =
                            dailyScraps.map { scrap ->
                                val announcement = scrap.internshipAnnouncement

                                DetailedScrap(
                                    announcementId =
                                        announcement.id
                                            ?: throw ScrapException(ScrapErrorCode.SCRAP_ID_NULL),
                                    companyImageUrl = announcement.company.logoUrl.value,
                                    title = announcement.title.value,
                                    workingPeriod = announcement.workingPeriod.toString(),
                                    isScrapped = true,
                                    hexColor = scrap.hexColor(),
                                    deadline = deadline,
                                    startYear = announcement.startDate.year.value,
                                    startMonth = announcement.startDate.month.value,
                                )
                            },
                    )
                }

        return DetailedMonthlyScrapResponse(dailyGroups = dailyGroups)
    }
}
