package com.terning.server.kotlin.application.auth

import com.terning.server.kotlin.application.auth.dto.SignInRequest
import com.terning.server.kotlin.application.auth.dto.SignInResponse
import com.terning.server.kotlin.application.auth.signin.AuthSignInService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val authSignInService: AuthSignInService,
) {
    fun signInUser(
        authAccessToken: String,
        signInRequest: SignInRequest,
    ): SignInResponse =
        authSignInService.signIn(
            authAccessToken = authAccessToken,
            request = signInRequest,
        )
}
