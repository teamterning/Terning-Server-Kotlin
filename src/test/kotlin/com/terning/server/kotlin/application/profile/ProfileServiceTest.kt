package com.terning.server.kotlin.application.profile

import com.terning.server.kotlin.domain.auth.Auth
import com.terning.server.kotlin.domain.auth.AuthRepository
import com.terning.server.kotlin.domain.auth.vo.AuthId
import com.terning.server.kotlin.domain.auth.vo.AuthType
import com.terning.server.kotlin.domain.auth.vo.RefreshToken
import com.terning.server.kotlin.domain.user.User
import com.terning.server.kotlin.domain.user.UserRepository
import com.terning.server.kotlin.domain.user.exception.UserErrorCode
import com.terning.server.kotlin.domain.user.exception.UserException
import com.terning.server.kotlin.domain.user.vo.ProfileImage
import com.terning.server.kotlin.domain.user.vo.UserName
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.Optional
import io.mockk.verify

class ProfileServiceTest {
    private val authRepository: AuthRepository = mockk()
    private val userRepository: UserRepository = mockk()

    private lateinit var profileService: ProfileService

    @BeforeEach
    fun setUp() {
        profileService =
            ProfileService(
                authRepository = authRepository,
                userRepository = userRepository,
            )
    }

    @Test
    @DisplayName("userId로 프로필 정보를 성공적으로 조회한다")
    fun getProfileInformation() {
        // given
        val user = User.of(name = "유빈", profile = "BASIC")
        val auth =
            Auth.of(
                user = user,
                authId = AuthId("123"),
                authType = AuthType.KAKAO,
                refreshToken = RefreshToken("refreshToken"),
            )
        val userId = 1L

        every { userRepository.findById(userId) } returns Optional.of(user)
        every { authRepository.findById(userId) } returns Optional.of(auth)

        // when
        val result = profileService.getUserProfile(userId)

        // then
        assertThat(result.name).isEqualTo("유빈")
        assertThat(result.profileImage).isEqualTo(ProfileImage.BASIC.value)
        assertThat(result.authType).isEqualTo(AuthType.KAKAO.value)
    }

    @Test
    @DisplayName("userId로 조회 실패 시 예외를 던진다")
    fun getProfileFailsIfUserNotFound() {
        // given
        val userId = 1L
        every { userRepository.findById(userId) } returns Optional.empty()

        // then
        val exception =
            assertThrows(UserException::class.java) {
                profileService.getUserProfile(userId)
            }

        assertThat(exception.errorCode).isEqualTo(UserErrorCode.NOT_FOUND_USER_EXCEPTION)
    }

    @Test
    @DisplayName("사용자가 입력한 정보로 프로필 수정이 된다")
    fun updateProfileSucceeds() {
        // given
        val userId = 1L
        val profileRequest = ProfileRequest(name = "이유빈", profileImage = "LUCKY")

        val user = mockk<User>(relaxed = true)

        every { userRepository.findById(userId) } returns Optional.of(user)

        // when
        profileService.updateUserProfile(userId, profileRequest)

        // then
        verify {
            user.updateProfile(
                newName = UserName.from("이유빈"),
                newProfileImage = ProfileImage.from("LUCKY"),
            )
        }
    }

    @Test
    @DisplayName("13자 이상의 이름이 들어오면 프로필 수정 오류가 발생한다")
    fun updateProfileFailsIfNameTooLong() {
        // given
        val userId = 1L
        val longName = "동해물과백두산이마르고닳도록"
        val profileRequest = ProfileRequest(name = longName, profileImage = "BASIC")
        val user = mockk<User>()

        every { userRepository.findById(userId) } returns Optional.of(user)

        // then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            profileService.updateUserProfile(userId, profileRequest)
        }

        assertThat(exception.message).contains("이름은 1~12자여야 합니다.")
    }

}
