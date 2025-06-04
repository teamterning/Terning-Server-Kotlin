package com.terning.server.kotlin.application.profile

import com.terning.server.kotlin.domain.auth.AuthRepository
import com.terning.server.kotlin.domain.auth.exception.AuthErrorCode
import com.terning.server.kotlin.domain.auth.exception.AuthException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProfileService(
    private val authRepository: AuthRepository,
) {
    @Transactional
    fun getProfile(userId: Long): ProfileResponse {
        val auth =
            authRepository.findByUserId(userId)
                .orElseThrow { AuthException(AuthErrorCode.NOT_FOUND_USER_EXCEPTION) }

        val user = auth.user

        return ProfileResponse(
            name = user.name(),
            profileImage = user.profileImage(),
            authType = auth.authType(),
        )
    }
}
