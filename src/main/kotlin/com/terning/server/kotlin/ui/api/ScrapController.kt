package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.ScrapService
import com.terning.server.kotlin.application.scrap.ScrapRequest
import com.terning.server.kotlin.application.scrap.ScrapUpdateRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/scraps")
class ScrapController(
    private val scrapService: ScrapService,
) {
    @PostMapping("/{internshipAnnouncementId}")
    fun scrap(
        // @AuthenticationPrincipal userId: Long,
        @PathVariable internshipAnnouncementId: Long,
        @RequestBody scrapRequest: ScrapRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        val userId: Long = 1 // 임시 userId

        scrapService.scrap(userId, internshipAnnouncementId, scrapRequest)

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

    @PatchMapping("/{internshipAnnouncementId}")
    fun updateScrap(
        // @AuthenticationPrincipal userId: Long,
        @PathVariable internshipAnnouncementId: Long,
        @RequestBody scrapUpdateRequest: ScrapUpdateRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        val userId: Long = 1 // 임시 userId

        scrapService.updateScrap(userId, internshipAnnouncementId, scrapUpdateRequest)

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

    @DeleteMapping("/{internshipAnnouncementId}")
    fun cancelScrap(
        // @AuthenticationPrincipal userId: Long,
        @PathVariable internshipAnnouncementId: Long,
    ): ResponseEntity<ApiResponse<Unit>> {
        val userId: Long = 1 // 임시 userId

        scrapService.cancelScrap(userId, internshipAnnouncementId)

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "스크랩 취소에 성공했습니다",
                result = Unit,
            )
        )
    }
}
