package com.terning.server.kotlin.application.auth.dto

data class SignUpResponse (
    val accessToken: String,
    val refreshToken: String,
    val userId: Long,
    val authType: String,
)
