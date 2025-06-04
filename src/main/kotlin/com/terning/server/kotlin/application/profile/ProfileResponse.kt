package com.terning.server.kotlin.application.profile

import com.terning.server.kotlin.domain.auth.vo.AuthType
import com.terning.server.kotlin.domain.user.vo.ProfileImage

data class ProfileResponse(
    val name: String,
    val profileImage: ProfileImage,
    val authType: AuthType,
)
