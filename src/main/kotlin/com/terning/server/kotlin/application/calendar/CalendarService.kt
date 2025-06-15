package com.terning.server.kotlin.application.calendar

import com.terning.server.kotlin.application.calendar.dto.DailyScrapsResponse
import com.terning.server.kotlin.application.calendar.dto.DetailedMonthlyScrapsResponse
import com.terning.server.kotlin.application.calendar.dto.MonthlyViewResponse
import com.terning.server.kotlin.domain.scrap.ScrapRepository
import com.terning.server.kotlin.domain.scrap.exception.ScrapErrorCode
import com.terning.server.kotlin.domain.scrap.exception.ScrapException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class CalendarService(
    private val scrapRepository: ScrapRepository,
    private val clock: Clock,
) {
    fun getMonthlyScraps(
        userId: Long,
        year: Int,
        month: Int,
    ): MonthlyViewResponse {
        val startDate = LocalDate.of(year, month, 1)
        val endDate = startDate.plusMonths(1).minusDays(1)

        val scraps =
            scrapRepository.findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(
                userId = userId,
                start = startDate,
                end = endDate,
            )

        val groupedByDeadline =
            scraps
                .groupBy { it.internshipAnnouncement.internshipAnnouncementDeadline.value }
                .toSortedMap()
                .map { (deadline, dailyScraps) ->
                    MonthlyViewResponse.DeadlineGroup(
                        deadline = deadline.toString(),
                        scraps =
                            dailyScraps.map { scrap ->
                                MonthlyViewResponse.ScrapSummary(
                                    scrapId = scrap.id ?: throw ScrapException(ScrapErrorCode.SCRAP_ID_NULL),
                                    title = scrap.internshipAnnouncement.title.value,
                                    color = scrap.hexColor(),
                                )
                            },
                    )
                }
        return MonthlyViewResponse(deadlines = groupedByDeadline)
    }

    fun getDetailedMonthlyScraps(
        userId: Long,
        year: Int,
        month: Int,
    ): List<DetailedMonthlyScrapsResponse> {
        val startDate = LocalDate.of(year, month, 1)
        val endDate = startDate.plusMonths(1).minusDays(1)

        val scraps =
            scrapRepository.findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(
                userId = userId,
                start = startDate,
                end = endDate,
            )

        return scraps.map { scrap ->
            DetailedMonthlyScrapsResponse.from(
                announcement = scrap.internshipAnnouncement,
                isScrapped = true,
                hexColor = scrap.hexColor(),
                clock = clock,
            )
        }
    }

    fun getDailyScraps(
        userId: Long,
        date: LocalDate,
    ): List<DailyScrapsResponse> {
        val scraps = scrapRepository.findScrapsByUserIdAndDeadlineOrderByDeadline(userId, date)

        return scraps.map { scrap ->
            DailyScrapsResponse.from(
                announcement = scrap.internshipAnnouncement,
                isScrapped = true,
                hexColor = scrap.hexColor(),
                clock = clock,
            )
        }
    }
}
