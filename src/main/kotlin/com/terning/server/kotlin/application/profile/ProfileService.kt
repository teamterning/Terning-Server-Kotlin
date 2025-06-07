package com.terning.server.kotlin.application.profile

import com.terning.server.kotlin.domain.auth.AuthRepository
import com.terning.server.kotlin.domain.auth.exception.AuthErrorCode
import com.terning.server.kotlin.domain.auth.exception.AuthException
import com.terning.server.kotlin.domain.user.UserRepository
import com.terning.server.kotlin.domain.user.exception.UserErrorCode
import com.terning.server.kotlin.domain.user.exception.UserException
import com.terning.server.kotlin.domain.user.vo.ProfileImage
import com.terning.server.kotlin.domain.user.vo.UserName
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProfileService(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun getUserProfile(userId: Long): ProfileResponse {
        val user =
            userRepository.findById(userId).orElseThrow {
                UserException(UserErrorCode.NOT_FOUND_USER_EXCEPTION)
            }

        val auth =
            authRepository.findById(userId).orElseThrow {
                AuthException(AuthErrorCode.NOT_FOUND_USER_EXCEPTION)
            }

        return ProfileResponse(
            name = user.name(),
            profileImage = user.profileImage().value,
            authType = auth.authType().value,
        )
    }

    @Transactional
    fun updateUserProfile(
        userId: Long,
        profileRequest: ProfileRequest,
    ) {
        val user =
            userRepository.findById(userId).orElseThrow {
                UserException(UserErrorCode.NOT_FOUND_USER_EXCEPTION)
            }

        val name = UserName.from(profileRequest.name)
        val profileImage = ProfileImage.from(profileRequest.profileImage)

        user.updateProfile(
            newName = name,
            newProfileImage = profileImage,
        )
    }
}
