package com.terning.server.kotlin.domain.auth.vo

import jakarta.persistence.Embeddable

@Embeddable
class RefreshToken private constructor(
    val value: String?,
) {
    override fun equals(other: Any?): Boolean = this === other || (other is RefreshToken && value == other.value)

    override fun hashCode(): Int = value?.hashCode() ?: 0

    override fun toString(): String = "RefreshToken(value=$value)"

    companion object {
        fun from(value: String?): RefreshToken = RefreshToken(value)
    }
}
