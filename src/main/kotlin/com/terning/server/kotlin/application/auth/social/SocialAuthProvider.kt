package com.terning.server.kotlin.application.auth.social

import com.terning.server.kotlin.domain.auth.vo.AuthType

interface SocialAuthProvider {
    fun getAuthId(authAccessToken: String): String

    fun supports(): AuthType
}
