package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.filter.FilterService
import com.terning.server.kotlin.application.filter.dto.CreateFilterRequest
import com.terning.server.kotlin.application.filter.dto.GetFilterResponse
import com.terning.server.kotlin.application.filter.dto.UpdateFilterRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class FilterController(
    private val filterService: FilterService,
) {
    @PostMapping("auth/sign-up/filter")
    fun createUserFilter(
        @AuthenticationPrincipal userId: Long,
        @RequestBody createFilterRequest: CreateFilterRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        filterService.createUserFilter(
            userId = userId,
            createFilterRequest = createFilterRequest,
        )

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "사용자 필터링 정보 생성에 성공하였습니다",
                result = Unit,
            ),
        )
    }

    @GetMapping("filters")
    fun getUserFilter(
        @AuthenticationPrincipal userId: Long,
    ): ResponseEntity<ApiResponse<GetFilterResponse>> {
        val response = filterService.getUserFilter(userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "사용자의 필터링 정보를 불러오는데 성공했습니다",
                result = response,
            ),
        )
    }

    @PutMapping("filters")
    fun updateUserFilter(
        @AuthenticationPrincipal userId: Long,
        @RequestBody updateFilterRequest: UpdateFilterRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        filterService.updateUserFilter(
            userId = userId,
            updateFilterRequest = updateFilterRequest,
        )

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "필터링 재설정에 성공했습니다",
                result = Unit,
            ),
        )
    }
}
