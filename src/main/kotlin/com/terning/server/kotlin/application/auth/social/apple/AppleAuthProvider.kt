package com.terning.server.kotlin.application.auth.social.apple

import com.terning.server.kotlin.application.auth.social.SocialAuthProvider
import com.terning.server.kotlin.domain.auth.vo.AuthType
import org.springframework.stereotype.Service

@Service
class AppleAuthProvider(
    private val appleAuthTokenValidator: AppleAuthTokenValidator,
) : SocialAuthProvider {
    override fun getAuthId(authAccessToken: String): String = appleAuthTokenValidator.extractAppleId(authAccessToken)

    override fun supports(): AuthType = AuthType.APPLE
}
