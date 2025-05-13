package com.terning.server.kotlin.domain.user

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class UserProfileImage(
    @Enumerated(EnumType.STRING)
    @Column(length = 12)
    val profileImage: ProfileImage,
)
