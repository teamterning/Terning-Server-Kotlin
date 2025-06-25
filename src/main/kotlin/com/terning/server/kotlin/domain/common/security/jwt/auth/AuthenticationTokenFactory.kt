package com.terning.server.kotlin.domain.common.security.jwt.auth

import com.terning.server.kotlin.domain.auth.Auth

object AuthenticationTokenFactory {
    fun create(auth: Auth): UserAuthentication = UserAuthentication(auth.user.id)
}
