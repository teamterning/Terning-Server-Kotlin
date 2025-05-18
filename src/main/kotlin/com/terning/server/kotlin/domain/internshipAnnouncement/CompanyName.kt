package com.terning.server.kotlin.domain.internshipAnnouncement

class CompanyName private constructor(
    val value: String,
) {
    init {
        validateNotBlank(value)
        validateMaxLength(value)
    }

    companion object {
        private const val MAX_LENGTH = 64

        fun from(value: String): CompanyName = CompanyName(value)

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
    }

    override fun equals(other: Any?): Boolean = this === other || (other is CompanyName && value == other.value)

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value
}
