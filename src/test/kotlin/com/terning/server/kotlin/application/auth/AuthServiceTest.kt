package com.terning.server.kotlin.application.auth

import com.terning.server.kotlin.application.auth.dto.SignInRequest
import com.terning.server.kotlin.application.auth.dto.SignInResponse
import com.terning.server.kotlin.application.auth.dto.SignUpRequest
import com.terning.server.kotlin.application.auth.social.SocialAuthProvider
import com.terning.server.kotlin.application.auth.social.SocialAuthServiceManager
import com.terning.server.kotlin.domain.auth.Auth
import com.terning.server.kotlin.domain.auth.AuthRepository
import com.terning.server.kotlin.domain.auth.vo.AuthId
import com.terning.server.kotlin.domain.auth.vo.AuthType
import com.terning.server.kotlin.domain.auth.vo.RefreshToken
import com.terning.server.kotlin.domain.auth.vo.Token
import com.terning.server.kotlin.domain.common.security.jwt.application.JwtTokenManager
import com.terning.server.kotlin.domain.user.User
import com.terning.server.kotlin.domain.user.UserRepository
import com.terning.server.kotlin.domain.user.vo.ProfileImage
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class AuthServiceTest {
    private val socialAuthServiceManager: SocialAuthServiceManager = mockk()
    private val jwtTokenManager: JwtTokenManager = mockk()
    private val authRepository: AuthRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private lateinit var authService: AuthService

    private val accessToken = "accessToken"
    private val refreshToken = "refreshToken"
    private val signInRequest = SignInRequest(authType = "KAKAO", fcmToken = "")
    private val authType = AuthType.KAKAO
    private val authId = "123456"
    private val kakaoProvider = mockk<SocialAuthProvider>()
    private val profileImage = ProfileImage.LUCKY.value

    @BeforeEach
    fun setup() {
        authService =
            AuthService(
                socialAuthServiceManager = socialAuthServiceManager,
                jwtTokenManager = jwtTokenManager,
                authRepository = authRepository,
                userRepository = userRepository,
            )
    }

    @Nested
    @DisplayName("signIn 메소드는")
    inner class SignIn {
        @Test
        fun `회원이 존재하지 않으면 null 토큰과 null 유저아이디를 반환한다`() {
            // given
            every { socialAuthServiceManager.getAuthService(authType) } returns kakaoProvider
            every { kakaoProvider.getAuthId(accessToken) } returns authId
            every { authRepository.findByAuthIdAndAuthType(AuthId.from(authId), authType) } returns null

            // when
            val result: SignInResponse =
                authService.signInUser(
                    socialAccessToken = accessToken,
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
            every { kakaoProvider.getAuthId(accessToken) } returns authId
            every { authRepository.findByAuthIdAndAuthType(authIdVo, authType) } returns auth
            every { jwtTokenManager.generateToken(user) } returns token
            every { auth.updateRefreshToken(any()) } just Runs

            // when
            val result = authService.signInUser(accessToken, signInRequest)

            // then
            assertEquals("newAccess", result.accessToken)
            assertEquals("newRefresh", result.refreshToken)
            assertEquals(authId, result.authId)
            assertEquals(authType.name, result.authType)
            assertEquals(user.id, result.userId)

            verify { auth.updateRefreshToken(RefreshToken.from("newRefresh")) }
        }
    }

    @Nested
    @DisplayName("signUp 메소드는")
    inner class SignUp {
        @Test
        fun `회원가입 시 토큰과 유저 정보를 반환한다`() {
            // given
            val request =
                SignUpRequest(
                    name = "이유빈",
                    profileImage = profileImage,
                    authType = authType.value,
                    fcmToken = "",
                )
            val token = Token(accessToken = accessToken, refreshToken = refreshToken)

            every { userRepository.save(any()) } answers {
                val user = firstArg<User>()
                val field = User::class.java.getDeclaredField("id")
                field.isAccessible = true
                field.set(user, 1L)
                user
            }
            every { authRepository.save(any()) } returns mockk()
            every { jwtTokenManager.generateToken(any()) } returns token

            // when
            val response = authService.signUpUser(authId = authId, signUpRequest = request)

            // then
            assertNotNull(response)
            assertEquals("accessToken", response.accessToken)
            assertEquals("refreshToken", response.refreshToken)
            assertEquals("KAKAO", response.authType)
        }
    }

    @Nested
    @DisplayName("signOut 메소드는")
    inner class SignOut {
        @Test
        fun `로그아웃 시 유저의 리프레시 토큰을 초기화한다`() {
            // given
            val userId = 1L
            val mockAuth = mockk<Auth>(relaxed = true)
            every { authRepository.findByUserId(userId) } returns mockAuth

            // when
            authService.singOut(userId)

            // then
            verify { mockAuth.resetRefreshToken() }
        }
    }
}
