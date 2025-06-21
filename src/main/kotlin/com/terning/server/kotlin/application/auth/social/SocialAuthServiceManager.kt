package com.terning.server.kotlin.application.auth.social

import com.terning.server.kotlin.domain.auth.vo.AuthType

class SocialAuthServiceManager private constructor(
    private val authServiceMap: Map<AuthType, SocialAuthProvider>,
) {
    fun getAuthService(authType: AuthType): SocialAuthProvider =
        authServiceMap[authType] ?: throw IllegalArgumentException("지원되지 않는 소셜 로그인 타입: $authType")

    companion object {
        fun create(providers: List<SocialAuthProvider>): SocialAuthServiceManager {
            val map = providers.associateBy { it.supports() }

            return SocialAuthServiceManager(map)
        }
    }
}
