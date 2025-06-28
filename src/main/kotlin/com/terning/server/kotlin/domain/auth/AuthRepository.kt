package com.terning.server.kotlin.domain.auth

import com.terning.server.kotlin.domain.auth.vo.AuthId
import com.terning.server.kotlin.domain.auth.vo.AuthType
import com.terning.server.kotlin.domain.auth.vo.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface AuthRepository : JpaRepository<Auth, Long> {
    fun findByAuthIdAndAuthType(
        authId: AuthId,
        authType: AuthType,
    ): Auth?

    fun findByUserId(userId: Long): Auth?

    fun findByRefreshToken(refreshToken: RefreshToken): Auth?
}
