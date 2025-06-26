package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.calendar.CalendarService
import com.terning.server.kotlin.application.calendar.dto.DailyScrapsResponse
import com.terning.server.kotlin.application.calendar.dto.DetailedMonthlyScrapsResponse
import com.terning.server.kotlin.application.calendar.dto.MonthlyViewResponse
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/calendar")
class CalendarController(
    private val calendarService: CalendarService,
) {
    @GetMapping("/daily")
    fun getDailyScraps(
        @AuthenticationPrincipal userId: Long,
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
    ): ResponseEntity<ApiResponse<List<DailyScrapsResponse>>> {
        val dailyScrapsResponse = calendarService.getDailyScraps(userId, date)
        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "일간 스크랩 공고 조회를 성공했습니다.",
                result = dailyScrapsResponse,
            ),
        )
    }

    @GetMapping("/monthly-list")
    fun getDetailedMonthlyScraps(
        @AuthenticationPrincipal userId: Long,
        @RequestParam("year") year: Int,
        @RequestParam("month") month: Int,
    ): ResponseEntity<ApiResponse<List<DetailedMonthlyScrapsResponse>>> {
        val detailedMonthlyScrapsResponse = calendarService.getDetailedMonthlyScraps(userId, year, month)
        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "월간 스크랩 공고 리스트 조회를 성공했습니다.",
                result = detailedMonthlyScrapsResponse,
            ),
        )
    }

    @GetMapping("/monthly-default")
    fun getMonthlyScraps(
        @AuthenticationPrincipal userId: Long,
        @RequestParam("year") year: Int,
        @RequestParam("month") month: Int,
    ): ResponseEntity<ApiResponse<MonthlyViewResponse>> {
        val monthlyViewResponse = calendarService.getMonthlyScraps(userId, year, month)
        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "월간 스크랩 공고 조회를 성공했습니다.",
                result = monthlyViewResponse,
            ),
        )
    }
}
