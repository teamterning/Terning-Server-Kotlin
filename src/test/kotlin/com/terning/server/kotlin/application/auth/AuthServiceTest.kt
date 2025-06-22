package com.terning.server.kotlin.application.auth

import com.terning.server.kotlin.application.auth.dto.SignInRequest
import com.terning.server.kotlin.application.auth.dto.SignInResponse
import com.terning.server.kotlin.application.auth.social.SocialAuthProvider
import com.terning.server.kotlin.application.auth.social.SocialAuthServiceManager
import com.terning.server.kotlin.domain.auth.Auth
import com.terning.server.kotlin.domain.auth.AuthRepository
import com.terning.server.kotlin.domain.auth.vo.*
import com.terning.server.kotlin.domain.common.security.jwt.application.JwtTokenManager
import com.terning.server.kotlin.domain.user.User
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthServiceTest {
    private val socialAuthServiceManager: SocialAuthServiceManager = mockk()
    private val jwtTokenManager: JwtTokenManager = mockk()
    private val authRepository: AuthRepository = mockk()
    private lateinit var authService: AuthService

    private val socialAccessToken = "socialAccessToken"
    private val signInRequest = SignInRequest(authType = "KAKAO", fcmToken = "")
    private val authType = AuthType.KAKAO
    private val authId = "123456"
    private val kakaoProvider = mockk<SocialAuthProvider>()

    @BeforeEach
    fun setup() {
        authService =
            AuthService(
                socialAuthServiceManager = socialAuthServiceManager,
                jwtTokenManager = jwtTokenManager,
                authRepository = authRepository,
            )
    }

    @Test
    fun `회원이 존재하지 않으면 null 토큰과 null 유저아이디를 반환한다`() {
        // given
        every { socialAuthServiceManager.getAuthService(authType) } returns kakaoProvider
        every { kakaoProvider.getAuthId(socialAccessToken) } returns authId
        every { authRepository.findByAuthIdAndAuthType(AuthId.from(authId), authType) } returns null

        // when
        val result: SignInResponse =
            authService.signInUser(
                socialAccessToken = socialAccessToken,
                signInRequest = signInRequest,
            )

        // then
        assertNull(result.accessToken)
        assertNull(result.userId)
        assertEquals(authId, result.authId)
        assertEquals(authType.name, result.authType)
    }

    @Test
    fun `회원이 존재하면 토큰과 유저아이디를 반환한다`() {
        // given
        val authIdVo = AuthId.from(authId)

        val user = mockk<User>(relaxed = true)
        val auth =
            spyk(
                Auth.of(
                    user = user,
                    authId = authIdVo,
                    authType = authType,
                    refreshToken = RefreshToken.from("old-token"),
                ),
            )
        val token =
            Token(
                accessToken = "newAccess",
                refreshToken = "newRefresh",
            )

        every { socialAuthServiceManager.getAuthService(authType) } returns kakaoProvider
        every { kakaoProvider.getAuthId(socialAccessToken) } returns authId
        every { authRepository.findByAuthIdAndAuthType(authIdVo, authType) } returns auth
        every { jwtTokenManager.generateToken(user) } returns token
        every { auth.updateRefreshToken(any()) } just Runs

        // when
        val result = authService.signInUser(socialAccessToken, signInRequest)

        // then
        assertEquals("newAccess", result.accessToken)
        assertEquals("newRefresh", result.refreshToken)
        assertEquals(authId, result.authId)
        assertEquals(authType.name, result.authType)
        assertEquals(user.id, result.userId)

        verify { auth.updateRefreshToken(RefreshToken.from("newRefresh")) }
    }
}
