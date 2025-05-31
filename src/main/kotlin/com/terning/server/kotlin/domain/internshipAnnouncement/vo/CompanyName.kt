package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import jakarta.persistence.Embeddable

@Embeddable
class CompanyName private constructor(
    val value: String,
) {
    init {
        validateNotBlank(value)
        validateMaxLength(value)
    }

    override fun equals(other: Any?): Boolean = this === other || (other is CompanyName && value == other.value)

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    private fun validateNotBlank(value: String) {
        if (value.isBlank()) {
            throw InternshipAnnouncementException(InternshipAnnouncementErrorCode.INVALID_COMPANY_NAME_EMPTY)
        }
    }

    private fun validateMaxLength(value: String) {
        if (value.length > MAX_LENGTH) {
            throw InternshipAnnouncementException(InternshipAnnouncementErrorCode.INVALID_COMPANY_NAME_TOO_LONG)
        }
    }

    companion object {
        private const val MAX_LENGTH = 64
        private const val DEFAULT_COMPANY_NAME = "터닝"

        fun from(value: String): CompanyName = CompanyName(value)
    }
}
