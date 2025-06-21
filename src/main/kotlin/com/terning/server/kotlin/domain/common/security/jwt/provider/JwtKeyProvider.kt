package com.terning.server.kotlin.domain.common.security.jwt.provider

import com.terning.server.kotlin.domain.common.config.ValueConfig
import io.jsonwebtoken.security.Keys
import javax.crypto.SecretKey

object JwtKeyProvider {
    fun getSigningKey(valueConfig: ValueConfig): SecretKey = Keys.hmacShaKeyFor(valueConfig.secretKey.toByteArray())
}
