package com.terning.server.kotlin.domain.auth

import com.terning.server.kotlin.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface AuthRepository : JpaRepository<Auth, Long> {
    fun user(user: User): MutableList<Auth>
}
