package com.terning.server.kotlin.domain.common.security.jwt.application

import com.terning.server.kotlin.domain.auth.Auth
import com.terning.server.kotlin.domain.auth.vo.Token
import com.terning.server.kotlin.domain.common.config.ValueConfig
import com.terning.server.kotlin.domain.common.security.jwt.auth.AuthenticationTokenFactory
import org.springframework.stereotype.Service

@Service
class JwtTokenManager(
    private val jwtTokenIssuer: JwtTokenIssuer,
    private val valueConfig: ValueConfig,
) {
    fun generateToken(auth: Auth): Token {
        val authentication = AuthenticationTokenFactory.create(auth)
        val accessTokenExpiration = valueConfig.accessTokenExpired
        val refreshTokenExpiration = valueConfig.refreshTokenExpired

        return Token(
            accessToken =
                jwtTokenIssuer.generateToken(
                    authentication = authentication,
                    expiration = accessTokenExpiration,
                ),
            refreshToken =
                jwtTokenIssuer.generateToken(
                    authentication = authentication,
                    expiration = refreshTokenExpiration,
                ),
        )
    }

    fun issueAccessToken(auth: Auth): Token {
        val authentication = AuthenticationTokenFactory.create(auth)
        val accessTokenExpiration = valueConfig.accessTokenExpired

        return Token(
            accessToken =
                jwtTokenIssuer.generateToken(
                    authentication = authentication,
                    expiration = accessTokenExpiration,
                ),
        )
    }
}
