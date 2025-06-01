package com.terning.server.kotlin.domain.user.vo

enum class ProfileImage(val value: String) {
    BASIC("basic"),
    LUCKY("lucky"),
    SMART("smart"),
    GLASS("glass"),
    CALENDAR("calendar"),
    PASSION("passion"),
    ;

    companion object {
        fun from(value: String): ProfileImage =
            entries.find { it.value.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("유효하지 않은 프로필 이미지입니다: $value")
    }
}
