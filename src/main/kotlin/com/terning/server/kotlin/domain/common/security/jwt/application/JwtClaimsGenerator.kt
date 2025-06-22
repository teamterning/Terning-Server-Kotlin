package com.terning.server.kotlin.domain.common.security.jwt.application

import com.terning.server.kotlin.domain.common.security.jwt.exception.JwtErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class JwtClaimsGenerator {
    fun generateClaims(authentication: Authentication): Claims = Jwts.claims(createClaimsMap(authentication))

    private fun createClaimsMap(authentication: Authentication): Map<String, Any> {
        val principal = authentication.principal

        if (principal is Long) {
            return mapOf(USER_ID_CLAIM to principal)
        }

        throw JwtException(JwtErrorCode.INVALID_USER_DETAILS_TYPE.message())
    }

    companion object {
        private const val USER_ID_CLAIM = "userId"
    }
}
