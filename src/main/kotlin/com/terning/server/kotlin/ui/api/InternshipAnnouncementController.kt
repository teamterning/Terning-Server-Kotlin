package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.internshipAnnouncement.InternshipAnnouncementService
import com.terning.server.kotlin.application.internshipAnnouncement.dto.HomeAnnouncementsResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/home")
class InternshipAnnouncementController(
    private val internshipAnnouncementService: InternshipAnnouncementService,
) {
    @GetMapping
    fun getInternshipAnnouncementsFilteredByUserFilter(
        // TODO: 실제 로그인된 사용자의 인증 정보 주입 필요
        // @AuthenticationPrincipal userId: Long,
        @RequestParam(name = "sortBy", required = false, defaultValue = "deadlineSoon")
        sortingCondition: String,
        @PageableDefault(size = 10) pageable: Pageable,
    ): ResponseEntity<ApiResponse<HomeAnnouncementsResponse>> {
        val authenticatedUserId: Long = 1L // TODO: 인증 시스템 연동 시 실제 유저 ID로 대체

        val internshipAnnouncementResponse: HomeAnnouncementsResponse =
            internshipAnnouncementService.getFilteredAnnouncements(
                userId = authenticatedUserId,
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
}
