package com.terning.server.kotlin.domain.common.config

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.nio.charset.StandardCharsets
import java.util.Base64

@Configuration
class ValueConfig(
    @Value("\${jwt.secret-key}")
    private var secretKeyRaw: String,

    @Value("\${jwt.kakao-url}")
    val kakaoUri: String,

    @Value("\${jwt.apple-url}")
    val appleUri: String,

    @Value("\${jwt.access-token-expired}")
    val accessTokenExpired: Long,

    @Value("\${jwt.refresh-token-expired}")
    val refreshTokenExpired: Long,
) {
    lateinit var secretKey: String

    @PostConstruct
    fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKeyRaw.toByteArray(StandardCharsets.UTF_8))
    }
}
