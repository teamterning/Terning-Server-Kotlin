package com.terning.server.kotlin.application.auth.dto

data class SignInRequest(
    val authType: String,
    val fcmToken: String,
)
