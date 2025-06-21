package com.terning.server.kotlin.application.search.dto

data class BannersView(
    val banners: List<Banner>,
)

data class Banner(
    val imageUrl: String,
    val link: String,
)
