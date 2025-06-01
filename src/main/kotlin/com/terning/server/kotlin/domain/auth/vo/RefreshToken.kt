package com.terning.server.kotlin.domain.auth.vo

import jakarta.persistence.Embeddable

@Embeddable
data class RefreshToken(
    val value: String?,
)
