package com.terning.server.kotlin.application.auth.dto

import com.terning.server.kotlin.domain.auth.vo.AuthType
import com.terning.server.kotlin.domain.auth.vo.Token

data class SignInResponse(
    val accessToken: String?,
    val refreshToken: String?,
    val userId: Long?,
    val authId: String,
    val authType: String,
    val fcmTokenReissueRequired: Boolean,
) {
    companion object {
        fun of(
            token: Token?,
            authId: String,
            userId: Long?,
            authType: AuthType,
            fcmTokenReissueRequired: Boolean = false,
        ): SignInResponse =
            SignInResponse(
                accessToken = token?.accessToken,
                refreshToken = token?.refreshToken,
                userId = userId,
                authId = authId,
                authType = authType.name,
                fcmTokenReissueRequired = fcmTokenReissueRequired,
            )
    }
}
