package com.terning.server.kotlin.domain.auth.vo

import jakarta.persistence.Embeddable

@Embeddable
class AuthId private constructor(
    val value: String,
) {
    override fun equals(other: Any?): Boolean = this === other || (other is AuthId && value == other.value)

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = "AuthId(value=$value)"

    companion object {
        fun from(value: String): AuthId = AuthId(value)
    }
}
