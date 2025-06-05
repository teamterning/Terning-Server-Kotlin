package com.terning.server.kotlin.application.profile

data class ProfileResponse(
    val name: String,
    val profileImage: String,
    val authType: String,
)
