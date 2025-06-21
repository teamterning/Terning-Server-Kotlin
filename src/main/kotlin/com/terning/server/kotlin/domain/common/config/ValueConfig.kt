package com.terning.server.kotlin.domain.common.config

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.nio.charset.StandardCharsets
import java.util.Base64

@Configuration
class ValueConfig(
    @Value("\${jwt.SECRET_KEY}")
    private var secretKeyRaw: String,

    @Value("\${jwt.KAKAO_URL}")
    val kakaoUri: String,

    @Value("\${jwt.APPLE_URL}")
    val appleUri: String,

    @Value("\${jwt.ACCESS_TOKEN_EXPIRED}")
    val accessTokenExpired: Long,

    @Value("\${jwt.REFRESH_TOKEN_EXPIRED}")
    val refreshTokenExpired: Long,
) {
    lateinit var secretKey: String

    @PostConstruct
    fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKeyRaw.toByteArray(StandardCharsets.UTF_8))
    }
}
