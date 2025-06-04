package com.terning.server.kotlin.application.profile

import com.terning.server.kotlin.domain.auth.Auth
import com.terning.server.kotlin.domain.auth.AuthRepository
import com.terning.server.kotlin.domain.auth.exception.AuthErrorCode
import com.terning.server.kotlin.domain.auth.exception.AuthException
import com.terning.server.kotlin.domain.auth.vo.AuthId
import com.terning.server.kotlin.domain.auth.vo.AuthType
import com.terning.server.kotlin.domain.auth.vo.RefreshToken
import com.terning.server.kotlin.domain.user.User
import com.terning.server.kotlin.domain.user.vo.ProfileImage
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

class ProfileServiceTest {

    private val authRepository: AuthRepository = mockk()

    private lateinit var profileService: ProfileService

    @BeforeEach
    fun setUp() {
        profileService = ProfileService(authRepository)
    }

    @Test
    @DisplayName("userId로 프로필 정보를 성공적으로 조회한다")
    fun getProfileInformation() {
        // given
        val user = User.of(name = "유빈", profile = "BASIC")
        val auth = Auth.of(
            user = user,
            authId = AuthId("123"),
            authType = AuthType.KAKAO,
            refreshToken = RefreshToken("refreshToken")
        )
        val userId = 1L

        every { authRepository.findByUserId(userId)}  returns Optional.of(auth)

        // when
        val result = profileService.getProfile(userId)

        // then
        assertThat(result.name).isEqualTo("유빈")
        assertThat(result.profileImage).isEqualTo(ProfileImage.BASIC)
        assertThat(result.authType).isEqualTo(AuthType.KAKAO)
    }

    @Test
    @DisplayName("userId로 조회 실패 시 예외를 던진다")
    fun getProfileFailsIfUserNotFound() {
        // given
        val userId = 1L
        every {authRepository.findByUserId(userId)} returns Optional.empty()

        // then
        val exception = assertThrows(AuthException::class.java) {
            profileService.getProfile(userId)
        }

        assertThat(exception.errorCode).isEqualTo(AuthErrorCode.NOT_FOUND_USER_EXCEPTION)
    }
}
