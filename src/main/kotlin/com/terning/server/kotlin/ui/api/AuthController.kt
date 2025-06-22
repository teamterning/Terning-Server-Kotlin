package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.auth.AuthService
import com.terning.server.kotlin.application.auth.dto.SignInRequest
import com.terning.server.kotlin.application.auth.dto.SignInResponse
import com.terning.server.kotlin.application.auth.dto.SignUpRequest
import com.terning.server.kotlin.application.auth.dto.SignUpResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/sign-in")
    fun signIn(
        @RequestHeader("Authorization")socialAccessToken: String,
        @RequestBody signInRequest: SignInRequest,
    ): ResponseEntity<ApiResponse<SignInResponse>> {
        val response =
            authService.signInUser(
                socialAccessToken = socialAccessToken,
                signInRequest = signInRequest,
            )

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "로그인에 성공했습니다.",
                result = response,
            ),
        )
    }

    @PostMapping("/sign-up")
    fun signUp(
        @RequestHeader("Authorization") authId: String,
        @RequestBody signUpRequest: SignUpRequest,
    ): ResponseEntity<ApiResponse<SignUpResponse>> {
        val response =
            authService.signUpUser(
                authId = authId,
                signUpRequest = signUpRequest,
            )

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "회원가입에 성공하였습니다.",
                result = response,
            ),
        )
    }

    @PostMapping("/logout")
    fun singOut(
        @AuthenticationPrincipal userId: Long,
    ): ResponseEntity<ApiResponse<Unit>> {
        authService.singOut(userId)

        return ResponseEntity.ok(
            ApiResponse.success(
                status = HttpStatus.OK,
                message = "로그아웃에 성공하였습니다.",
                result = Unit,
            ),
        )
    }
}
