package com.terning.server.kotlin.domain.user.vo

import jakarta.persistence.Embeddable

@Embeddable
class UserName(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { ERROR_EMPTY }
        require(value.length in MIN_LENGTH..MAX_LENGTH) { ERROR_LENGTH }
    }

    override fun equals(other: Any?): Boolean = this === other || (other is UserName && this.value == other.value)

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    companion object {
        private const val MIN_LENGTH = 1
        private const val MAX_LENGTH = 12

        private const val ERROR_EMPTY = "이름은 공백일 수 없습니다."
        private const val ERROR_LENGTH = "이름은 $MIN_LENGTH~${MAX_LENGTH}자여야 합니다."
    }
}
