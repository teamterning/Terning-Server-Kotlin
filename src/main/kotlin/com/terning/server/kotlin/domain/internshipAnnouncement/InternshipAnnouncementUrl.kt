package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Embeddable
import java.net.URI
import java.net.URISyntaxException

@Embeddable
class InternshipAnnouncementUrl private constructor(
    val value: String,
) {
    init {
        validateUrl(value)
    }

    protected constructor() : this(DEFAULT_URL)

    override fun equals(other: Any?): Boolean =
        this === other || (other is InternshipAnnouncementUrl && value == other.value)

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    private fun validateUrl(value: String) {
        try {
            val uri = URI(value)
            val scheme = uri.scheme?.lowercase()
            if (scheme !in ALLOWED_SCHEMES) {
                throw InternshipException(InternshipErrorCode.UNSUPPORTED_ANNOUNCEMENT_URL_SCHEME)
            }
        } catch (e: URISyntaxException) {
            throw InternshipException(InternshipErrorCode.INVALID_ANNOUNCEMENT_URL_FORMAT)
        }
    }

    companion object {
        private val ALLOWED_SCHEMES = setOf("http", "https")
        private const val DEFAULT_URL = "http://default"

        fun from(value: String): InternshipAnnouncementUrl = InternshipAnnouncementUrl(value)
    }
}
