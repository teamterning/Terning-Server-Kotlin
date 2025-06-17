package com.terning.server.kotlin.application.onboarding.dto

data class SignInRequest (
    val authType: String,
    val fcmToken: String,
)
