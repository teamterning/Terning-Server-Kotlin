package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.home.HomeService
import com.terning.server.kotlin.application.home.dto.HomeResponse
import com.terning.server.kotlin.application.home.dto.UpcomingDeadlineScrapResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/home")
class HomeController(
    private val homeService: HomeService,
) {
    @GetMapping
    fun getInternshipAnnouncementsFilteredByUserFilter(
        @AuthenticationPrincipal userId: Long,
        @RequestParam(name = "sortBy", required = false, defaultValue = "deadlineSoon")
        sortingCondition: String,
        @PageableDefault(size = 10) pageable: Pageable,
    ): ResponseEntity<ApiResponse<HomeResponse>> {
        val internshipAnnouncementResponse: HomeResponse =
            homeService.getFilteredAnnouncements(
                userId = userId,
                sortBy = sortingCondition,
                pageable = pageable,
            )

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "인턴 공고 불러오기를 성공했습니다",
                result = internshipAnnouncementResponse,
            ),
        )
    }

    @GetMapping("/upcoming")
    fun getUpcomingDeadlineScraps(
        @AuthenticationPrincipal userId: Long,
    ): ResponseEntity<ApiResponse<UpcomingDeadlineScrapResponse>> {
        val response = homeService.findUpcomingDeadlineScraps(userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = response.message,
                result = response,
            ),
        )
    }
}
