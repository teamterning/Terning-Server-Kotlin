package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.search.SearchService
import com.terning.server.kotlin.application.search.dto.ScrapCountResponse
import com.terning.server.kotlin.application.search.dto.SearchPageResponse
import com.terning.server.kotlin.application.search.dto.ViewCountResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class SearchController(
    private val searchService: SearchService,
) {
    @GetMapping
    fun search(
        // TODO: @AuthenticationPrincipal userId: Long,
        @RequestParam(required = false) keyword: String?,
        @RequestParam(defaultValue = "DEADLINE_SOON") sortBy: String,
        @PageableDefault(size = 10) pageable: Pageable,
    ): ResponseEntity<ApiResponse<SearchPageResponse>> {
        val userId: Long = 1 // TODO: @AuthenticationPrincipal 구현 시 제거

        val response =
            searchService.search(
                userId = userId,
                keyword = keyword,
                sortBy = sortBy,
                pageable = pageable,
            )

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "검색에 성공했습니다.",
                result = response,
            ),
        )
    }

    @GetMapping("/views")
    fun getMostViewedAnnouncements(
        // TODO: @AuthenticationPrincipal userId: Long,
    ): ResponseEntity<ApiResponse<ViewCountResponse>> {
        val userId: Long = 1 // TODO: @AuthenticationPrincipal 구현 시 제거

        val response = searchService.getMostViewedAnnouncements(userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "탐색 > 조회수 많은 공고를 조회하는데 성공했습니다",
                result = response,
            ),
        )
    }

    @GetMapping("/scraps")
    fun getMostScrappedAnnouncements(
        // TODO: @AuthenticationPrincipal userId: Long,
    ): ResponseEntity<ApiResponse<ScrapCountResponse>> {
        val userId: Long = 1 // TODO: @AuthenticationPrincipal 구현 시 제거

        val response = searchService.getMostScrappedAnnouncements(userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "탐색 > 스크랩 수 많은 공고를 조회하는데 성공했습니다",
                result = response,
            ),
        )
    }
}
