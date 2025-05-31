package com.terning.server.kotlin.domain.banner.vo

import jakarta.persistence.Embeddable

@Embeddable
data class ImageUrl(
    val value: String,
)
