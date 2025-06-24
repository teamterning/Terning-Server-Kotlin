package com.terning.server.kotlin.application.auth

import com.terning.server.kotlin.application.auth.dto.SignInRequest
import com.terning.server.kotlin.application.auth.dto.SignInResponse
import com.terning.server.kotlin.application.auth.dto.SignUpRequest
import com.terning.server.kotlin.application.auth.dto.SignUpResponse
import com.terning.server.kotlin.application.auth.social.SocialAuthServiceManager
import com.terning.server.kotlin.domain.auth.Auth
import com.terning.server.kotlin.domain.auth.AuthRepository
import com.terning.server.kotlin.domain.auth.exception.AuthErrorCode
import com.terning.server.kotlin.domain.auth.exception.AuthException
import com.terning.server.kotlin.domain.auth.vo.AuthId
import com.terning.server.kotlin.domain.auth.vo.AuthType
import com.terning.server.kotlin.domain.auth.vo.RefreshToken
import com.terning.server.kotlin.domain.common.security.jwt.application.JwtTokenManager
import com.terning.server.kotlin.domain.user.User
import com.terning.server.kotlin.domain.user.UserRepository
import com.terning.server.kotlin.domain.user.exception.UserErrorCode
import com.terning.server.kotlin.domain.user.exception.UserException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val socialAuthServiceManager: SocialAuthServiceManager,
    private val jwtTokenManager: JwtTokenManager,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun signInUser(
        socialAccessToken: String,
        signInRequest: SignInRequest,
    ): SignInResponse {
        val authType = AuthType.from(signInRequest.authType)
        val authId =
            socialAuthServiceManager.getAuthService(
                authType = authType,
            ).getAuthId(
                authAccessToken = socialAccessToken,
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
            newRefreshToken = RefreshToken.from(token.refreshToken),
        )

        return SignInResponse.of(
            token = token,
            authId = authId,
            authType = authType,
            userId = user.id,
        )
    }

    @Transactional
    fun signUpUser(
        authId: String,
        signUpRequest: SignUpRequest,
    ): SignUpResponse {
        val tokenWithoutBearer = authId.removePrefix("$BEARER ").trim()
        val user =
            User.of(
                name = signUpRequest.name,
                profile = signUpRequest.profileImage,
            )
        val auth =
            Auth.of(
                user = user,
                authId = AuthId.from(tokenWithoutBearer),
                authType = AuthType.from(signUpRequest.authType),
                refreshToken = RefreshToken.from(null),
            )

        userRepository.save(user)
        authRepository.save(auth)

        val token = jwtTokenManager.generateToken(user)

        auth.updateRefreshToken(RefreshToken.from(token.refreshToken))

        // TODO : ApplicationEventPublisher 구현

        return SignUpResponse.from(
            token = token,
            userId = user.id ?: throw UserException(UserErrorCode.USER_NOT_FOUND),
            authType = auth.authType(),
        )
    }

    @Transactional
    fun signOut(userId: Long) {
        val auth = authRepository.findByUserId(userId) ?: throw AuthException(AuthErrorCode.NOT_FOUND_USER_EXCEPTION)

        auth.resetRefreshToken()
    }

    @Transactional
    fun withdraw(userId: Long) {
        val user =
            userRepository.findById(userId).orElseThrow {
                UserException(UserErrorCode.USER_NOT_FOUND)
            }

        userRepository.delete(user)
    }

    companion object {
        private const val BEARER = "Bearer"
    }
}
