package com.terning.server.kotlin.domain.auth

import com.terning.server.kotlin.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface AuthRepository : JpaRepository<Auth, String> {
    fun user(user: User): MutableList<Auth>

    fun findByUserId(userId: Long): Optional<Auth>
}
