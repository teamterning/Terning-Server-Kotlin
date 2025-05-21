package com.terning.server.kotlin.domain.banner

import jakarta.persistence.Column

data class ImageUrl(
    @Column(length = 255)
    val value: String,
)
