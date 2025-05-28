package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipException
import jakarta.persistence.Embeddable

@Embeddable
class InternshipTitle private constructor(
    val value: String,
) {
    init {
        validateNotBlank(value)
        validateMaxLength(value)
    }

    protected constructor() : this("")

    override fun equals(other: Any?): Boolean = this === other || (other is InternshipTitle && value == other.value)

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    private fun validateNotBlank(value: String) {
        if (value.isBlank()) {
            throw InternshipException(InternshipErrorCode.INVALID_INTERNSHIP_TITLE_EMPTY)
        }
    }

    private fun validateMaxLength(value: String) {
        if (value.length > MAX_LENGTH) {
            throw InternshipException(InternshipErrorCode.INVALID_INTERNSHIP_TITLE_TOO_LONG)
        }
    }

    companion object {
        private const val MAX_LENGTH = 64

        fun from(value: String): InternshipTitle = InternshipTitle(value)
    }
}
