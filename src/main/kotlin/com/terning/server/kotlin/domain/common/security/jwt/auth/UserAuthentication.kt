package com.terning.server.kotlin.domain.common.security.jwt.auth

import org.springframework.security.authentication.AbstractAuthenticationToken

class UserAuthentication(
    private val userId: Long?,
) : AbstractAuthenticationToken(null) {
    init {
        isAuthenticated = true
    }

    override fun getPrincipal(): Any? = userId

    override fun getCredentials(): Any? = null
}
