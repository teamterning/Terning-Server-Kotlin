package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Embeddable

@Embeddable
class InternshipAnnouncementUrl private constructor(
    val value: String,
) {
    init {
        validateUrl(value)
    }

    companion object {
        fun from(value: String): InternshipAnnouncementUrl = InternshipAnnouncementUrl(value)

        private fun validateUrl(value: String) {
            if (!value.startsWith("http")) {
                throw InternshipException(InternshipErrorCode.INVALID_ANNOUNCEMENT_URL)
            }
        }
    }

    override fun equals(other: Any?): Boolean = this === other || (other is InternshipAnnouncementUrl && value == other.value)

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value
}
