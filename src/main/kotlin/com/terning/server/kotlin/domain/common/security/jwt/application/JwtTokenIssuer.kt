package com.terning.server.kotlin.domain.common.security.jwt.application

import io.jsonwebtoken.Claims
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtTokenIssuer(
    private val jwtClaimsGenerator: JwtClaimsGenerator,
    private val jwtSigner: JwtSigner,
) {
    fun generateToken(
        authentication: Authentication,
        expiration: Long,
    ): String {
        val claims: Claims = jwtClaimsGenerator.generateClaims(authentication)

        return jwtSigner.sign(
            claims = claims,
            expiration = expiration,
        )
    }
}
