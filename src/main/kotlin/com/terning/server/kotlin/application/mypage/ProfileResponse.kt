package com.terning.server.kotlin.application.mypage

data class ProfileResponse(
    val name: String,
    val profileImage: String,
    val authType: String,
)
