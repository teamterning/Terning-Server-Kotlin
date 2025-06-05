package com.terning.server.kotlin.domain.auth

import org.springframework.data.jpa.repository.JpaRepository

interface AuthRepository : JpaRepository<Auth, Long>
