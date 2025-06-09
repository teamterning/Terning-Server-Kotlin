package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.filter.FilterResponse
import com.terning.server.kotlin.application.filter.FilterService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class FilterController(
    private val filterService: FilterService,
) {
    @GetMapping("filters")
    fun getUserFilter(
        // TODO : @AuthenticationPrincipal userId: Long,
    ): ResponseEntity<ApiResponse<FilterResponse>> {
        val userId: Long = 1 // TODO: @AuthenticationPrincipal 구현 시 제거

        val response = filterService.getUserFilter(userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "사용자의 필터링 정보를 불러오는데 성공했습니다",
                result = response,
            ),
        )
    }
}
