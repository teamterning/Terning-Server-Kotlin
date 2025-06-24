package com.terning.server.kotlin.application.auth.dto

data class SignUpRequest(
    val name: String,
    val profileImage: String,
    val authType: String,
    val fcmToken: String,
)
