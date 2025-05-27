package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Embeddable

@Embeddable
class CompanyName private constructor(
    val value: String,
) {

    protected constructor() : this("터닝")

    init {
        validateNotBlank(value)
        validateMaxLength(value)
    }

    override fun equals(other: Any?): Boolean =
        this === other || (other is CompanyName && value == other.value)

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    private fun validateNotBlank(value: String) {
        if (value.isBlank()) {
            throw InternshipException(InternshipErrorCode.INVALID_COMPANY_NAME_EMPTY)
        }
    }

    private fun validateMaxLength(value: String) {
        if (value.length > MAX_LENGTH) {
            throw InternshipException(InternshipErrorCode.INVALID_COMPANY_NAME_TOO_LONG)
        }
    }

    companion object {
        private const val MAX_LENGTH = 64

        fun from(value: String): CompanyName = CompanyName(value)
    }
}
