package com.terning.server.kotlin.application.auth.dto

import com.terning.server.kotlin.domain.auth.vo.AuthType
import com.terning.server.kotlin.domain.auth.vo.Token

data class SignUpResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: Long,
    val authType: String,
) {
    companion object {
        fun from(
            token: Token,
            userId: Long,
            authType: AuthType,
        ): SignUpResponse =
            SignUpResponse(
                accessToken = token.accessToken,
                refreshToken = token.refreshToken,
                userId = userId,
                authType = authType.name,
            )
    }
}
