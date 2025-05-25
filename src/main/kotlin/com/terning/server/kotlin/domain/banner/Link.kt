package com.terning.server.kotlin.domain.banner

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class Link(
    @Column(length = 255)
    val value: String,
)
