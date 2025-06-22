package com.terning.server.kotlin.domain.auth.vo

data class Token(
    val accessToken: String,
    val refreshToken: String,
)
