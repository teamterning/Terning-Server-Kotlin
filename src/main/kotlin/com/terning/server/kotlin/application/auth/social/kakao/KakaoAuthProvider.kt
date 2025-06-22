package com.terning.server.kotlin.application.auth.social.kakao

import com.terning.server.kotlin.application.auth.social.SocialAuthProvider
import com.terning.server.kotlin.domain.auth.vo.AuthType
import org.springframework.stereotype.Service

@Service
class KakaoAuthProvider(
    private val kakaoAuthTokenValidator: KakaoAuthTokenValidator,
) : SocialAuthProvider {
    override fun getAuthId(authAccessToken: String): String = kakaoAuthTokenValidator.extractKakaoId(authAccessToken)

    override fun supports(): AuthType = AuthType.KAKAO
}
