package com.terning.server.kotlin.ui.api

import com.terning.server.kotlin.application.auth.AuthService
import com.terning.server.kotlin.application.auth.dto.SignInRequest
import com.terning.server.kotlin.application.auth.dto.SignInResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/sign-in")
    fun singIn(
        @RequestHeader("Authorization")authAccessToken: String,
        @RequestBody signInRequest: SignInRequest,
    ): ResponseEntity<ApiResponse<SignInResponse>> {
        val response =
            authService.signInUser(
                authAccessToken = authAccessToken,
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
}
