package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.mypage.MyPageService
import com.terning.server.kotlin.application.mypage.ProfileRequest
import com.terning.server.kotlin.application.mypage.ProfileResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/mypage")
class MyPageController(
    private val mypageService: MyPageService,
) {
    @GetMapping("/profile")
    fun getProfile(
        @AuthenticationPrincipal userId: Long,
    ): ResponseEntity<ApiResponse<ProfileResponse>> {
        val response = mypageService.getUserProfile(userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "마이페이지 > 프로필 정보 불러오기를 성공했습니다",
                result = response,
            ),
        )
    }

    @PatchMapping("/profile")
    fun updateProfile(
        @AuthenticationPrincipal userId: Long,
        @RequestBody profileRequest: ProfileRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        mypageService.updateUserProfile(
            userId = userId,
            profileRequest = profileRequest,
        )

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "프로필 수정에 성공했습니다",
                result = Unit,
            ),
        )
    }
}
