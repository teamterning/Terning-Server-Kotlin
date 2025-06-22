package com.terning.server.kotlin.domain.common.security.jwt.auth

import com.terning.server.kotlin.domain.user.User

object AuthenticationTokenFactory {
    fun create(user: User): UserAuthentication = UserAuthentication(user.id)
}
