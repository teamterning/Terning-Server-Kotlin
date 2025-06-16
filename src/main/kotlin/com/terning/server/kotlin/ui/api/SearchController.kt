package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.search.SearchService
import com.terning.server.kotlin.application.search.dto.SearchPageResponse
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
}
