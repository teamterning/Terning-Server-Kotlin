package com.terning.server.kotlin.domain.auth

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class RefreshToken(
    @Column(length = 255)
    val value: String?,
)
