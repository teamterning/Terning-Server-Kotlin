package com.terning.server.kotlin.domain.common.security.jwt.application

import com.terning.server.kotlin.domain.common.config.ValueConfig
import com.terning.server.kotlin.domain.common.security.jwt.provider.JwtKeyProvider
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtSigner(
    private val valueConfig: ValueConfig,
) {
    fun sign(
        claims: Claims,
        expiration: Long,
    ): String =
        Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(JwtKeyProvider.getSigningKey(valueConfig))
            .compact()
}
