package com.terning.server.kotlin.domain.user

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class UserName(
    @Column(length = 12)
    val value: String,
) {
    init {
        require(value.length in MIN_LENGTH..MAX_LENGTH) { "이름은 1~12자여야 합니다" }
    }

    companion object {
        private const val MIN_LENGTH: Int = 1
        private const val MAX_LENGTH: Int = 12
    }
}
