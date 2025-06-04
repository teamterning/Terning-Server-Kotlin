package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.profile.ProfileResponse
import com.terning.server.kotlin.application.profile.ProfileService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class ProfileController(
    private val profileService: ProfileService,
) {
    @GetMapping("mypage/profile")
    fun getProfile(
        // TODO : @AuthenticationPrincipal userId: Long,
    ): ResponseEntity<ApiResponse<ProfileResponse>> {
        val userId: Long = 1 // TODO: @AuthenticationPrincipal 구현 시 제거

        val response = profileService.getProfile(userId)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse.success(
                    status = HttpStatus.OK,
                    message = "마이페이지 > 프로필 정보 불러오기를 성공했습니다",
                    result =
                        ProfileResponse(
                            name = response.name,
                            profileImage = response.profileImage,
                            authType = response.authType,
                        ),
                ),
            )
    }
}
