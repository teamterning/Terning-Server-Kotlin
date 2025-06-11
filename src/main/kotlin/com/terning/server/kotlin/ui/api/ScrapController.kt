package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.scrap.ScrapService
import com.terning.server.kotlin.application.scrap.dto.DetailedMonthlyScrapResponse
import com.terning.server.kotlin.application.scrap.dto.DetailedScrap
import com.terning.server.kotlin.application.scrap.dto.MonthlyScrapDeadlineResponse
import com.terning.server.kotlin.application.scrap.dto.ScrapRequest
import com.terning.server.kotlin.application.scrap.dto.ScrapUpdateRequest
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1")
class ScrapController(
    private val scrapService: ScrapService,
) {
    @GetMapping("/calendar/daily")
    fun dailyScraps(
        // TODO: @AuthenticationPrincipal userId: Long,
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
    ): ResponseEntity<ApiResponse<List<DetailedScrap>>> {
        val userId: Long = 1 // TODO: @AuthenticationPrincipal 구현 시 교체

        val dailyScraps = scrapService.dailyScraps(userId, date)

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "캘린더 > (일간) 스크랩 된 공고 정보 불러오기를 성공했습니다",
                result = dailyScraps,
            ),
        )
    }

    @GetMapping("/calendar/monthly-list")
    fun monthlyScrapsAsList(
        // TODO: @AuthenticationPrincipal userId: Long,
        @RequestParam("year") year: Int,
        @RequestParam("month") month: Int,
    ): ResponseEntity<ApiResponse<DetailedMonthlyScrapResponse>> {
        val userId: Long = 1 // TODO: @AuthenticationPrincipal 구현 시 제거

        val response =
            scrapService.detailedMonthlyScraps(
                userId = userId,
                year = year,
                month = month,
            )

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "캘린더 > (월간) 스크랩 된 공고 정보 (리스트) 불러오기를 성공했습니다",
                result = response,
            ),
        )
    }

    @GetMapping("/calendar/monthly-default")
    fun monthlyScraps(
        // TODO: @AuthenticationPrincipal userId: Long,
        @RequestParam("year") year: Int,
        @RequestParam("month") month: Int,
    ): ResponseEntity<ApiResponse<MonthlyScrapDeadlineResponse>> {
        val userId: Long = 1 // TODO: @AuthenticationPrincipal 구현 시 제거

        val monthlyScrapDeadlineResponse =
            scrapService.monthlyScrapDeadlines(
                userId = userId,
                year = year,
                month = month,
            )

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "캘린더 > (월간) 스크랩 된 공고 정보 불러오기를 성공했습니다",
                result = monthlyScrapDeadlineResponse,
            ),
        )
    }

    @PostMapping("/scraps/{internshipAnnouncementId}")
    fun scrap(
        // TODO: @AuthenticationPrincipal userId: Long,
        @PathVariable internshipAnnouncementId: Long,
        @RequestBody scrapRequest: ScrapRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        val userId: Long = 1 // TODO: @AuthenticationPrincipal 구현 시 제거

        scrapService.scrap(
            userId = userId,
            internshipAnnouncementId = internshipAnnouncementId,
            scrapRequest = scrapRequest,
        )

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    status = HttpStatus.CREATED,
                    message = "스크랩 추가에 성공했습니다",
                    result = Unit,
                ),
            )
    }

    @PatchMapping("/scraps/{internshipAnnouncementId}")
    fun updateScrap(
        // TODO: @AuthenticationPrincipal userId: Long,
        @PathVariable internshipAnnouncementId: Long,
        @RequestBody scrapUpdateRequest: ScrapUpdateRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        val userId: Long = 1 // TODO: @AuthenticationPrincipal 구현 시 제거

        scrapService.updateScrap(
            userId = userId,
            internshipAnnouncementId = internshipAnnouncementId,
            scrapUpdateRequest = scrapUpdateRequest,
        )

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                ApiResponse.success(
                    status = HttpStatus.OK,
                    message = "스크랩 수정에 성공했습니다",
                    result = Unit,
                ),
            )
    }

    @DeleteMapping("/scraps/{internshipAnnouncementId}")
    fun cancelScrap(
        // @AuthenticationPrincipal userId: Long,
        @PathVariable internshipAnnouncementId: Long,
    ): ResponseEntity<ApiResponse<Unit>> {
        val userId: Long = 1 // 임시 userId

        scrapService.cancelScrap(
            userId = userId,
            internshipAnnouncementId = internshipAnnouncementId,
        )

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "스크랩 취소에 성공했습니다",
                result = Unit,
            ),
        )
    }
}
