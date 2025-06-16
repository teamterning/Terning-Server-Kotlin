package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.announcement.AnnouncementService
import com.terning.server.kotlin.application.announcement.dto.DetailAnnouncementResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/announcements")
class AnnouncementController(
    private val announcementService: AnnouncementService,
) {
    @GetMapping("/{internshipAnnouncementId}")
    fun getDetailInternshipAnnouncement(
        // TODO: @AuthenticationPrincipal userId: Long,
        @PathVariable internshipAnnouncementId: Long,
    ): ResponseEntity<ApiResponse<DetailAnnouncementResponse>> {
        val userId: Long = 1 // TODO: @AuthenticationPrincipal 구현 시 제거

        val response =
            announcementService.getDetailAnnouncement(
                userId = userId,
                internshipAnnouncementId = internshipAnnouncementId,
            )

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "공고 상세 정보 불러오기에 성공했습니다",
                result = response,
            ),
        )
    }
}
