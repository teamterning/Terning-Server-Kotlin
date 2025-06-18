package com.terning.server.kotlin.application.auth

import com.terning.server.kotlin.application.auth.dto.SignInRequest
import com.terning.server.kotlin.application.auth.dto.SignInResponse
import com.terning.server.kotlin.application.auth.social.SocialAuthServiceManager
import com.terning.server.kotlin.domain.auth.AuthRepository
import com.terning.server.kotlin.domain.auth.vo.AuthId
import com.terning.server.kotlin.domain.auth.vo.AuthType
import com.terning.server.kotlin.domain.auth.vo.RefreshToken
import com.terning.server.kotlin.domain.common.security.jwt.application.JwtTokenManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val socialAuthServiceManager: SocialAuthServiceManager,
    private val jwtTokenManager: JwtTokenManager,
    private val authRepository: AuthRepository,
) {
    fun signInUser(
        authAccessToken: String,
        signInRequest: SignInRequest,
    ): SignInResponse {
        val authType = AuthType.from(signInRequest.authType)
        val authId =
            socialAuthServiceManager.getAuthService(
                authType = authType,
            ).getAuthId(
                authAccessToken = authAccessToken,
            )
        val auth =
            authRepository.findByAuthIdAndAuthType(
                authId = AuthId.from(authId),
                authType = authType,
            )

        if (auth == null) {
            return SignInResponse.of(
                token = null,
                userId = null,
                authId = authId,
                authType = authType,
            )
        }

        val user = auth.user
        val token = jwtTokenManager.generateToken(user)

        auth.updateRefreshToken(
            newRefreshToken = RefreshToken.from(token.refreshToken)
        )

        return SignInResponse.of(
            token = token,
            authId = authId,
            authType = authType,
            userId = user.id,
        )
    }
}
