package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.ScrapService
import com.terning.server.kotlin.application.scrap.ScrapRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
}
