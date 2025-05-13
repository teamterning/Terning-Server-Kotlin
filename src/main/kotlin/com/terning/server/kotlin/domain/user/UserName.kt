package com.terning.server.kotlin.domain.user

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class UserName(
    @Column(length = 12)
    val value: String
) {
    init {
        require(value.length in 1..12) { "이름은 1~12자여야 합니다" }
    }
}
