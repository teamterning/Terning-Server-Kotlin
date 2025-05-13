package com.terning.server.kotlin.domain.user

enum class ProfileImage(val value: String) {
    BASIC("basic"),
    LUCKY("lucky"),
    SMART("smart"),
    GLASS("glass"),
    CALENDAR("calendar"),
    PASSION("passion"),
    ;

    companion object {
        fun fromValue(value: String): ProfileImage =
            entries.find { image ->
                image.value.equals(value, ignoreCase = true)
            } ?: throw IllegalArgumentException("Invalid profile image: $value")
    }
}
