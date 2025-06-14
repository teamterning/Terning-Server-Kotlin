package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.scrap.ScrapService
import com.terning.server.kotlin.application.scrap.dto.ScrapRequest
import com.terning.server.kotlin.application.scrap.dto.ScrapUpdateRequest
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
                    message = "스크랩 추가에 성공했습니다.",
                    result = Unit,
                ),
            )
    }

    @PatchMapping("/{internshipAnnouncementId}")
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

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "스크랩 수정을 성공했습니다.",
                result = Unit,
            ),
        )
    }

    @DeleteMapping("/{internshipAnnouncementId}")
    fun cancelScrap(
        // TODO: @AuthenticationPrincipal userId: Long,
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
                message = "스크랩 취소를 성공했습니다.",
                result = Unit,
            ),
        )
    }
}
