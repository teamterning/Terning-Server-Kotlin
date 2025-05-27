package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Embeddable
import java.net.URI
import java.net.URISyntaxException

@Embeddable
class CompanyLogoUrl private constructor(
    val value: String,
) {
    protected constructor() : this("http://default-logo-url.com")

    init {
        validateUrl(value)
    }

    override fun equals(other: Any?): Boolean = this === other || (other is CompanyLogoUrl && value == other.value)

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    private fun validateUrl(value: String) {
        val uri = parseUri(value)
        validateScheme(uri)
    }

    private fun parseUri(value: String): URI {
        return try {
            URI(value)
        } catch (e: URISyntaxException) {
            throw InternshipException(InternshipErrorCode.INVALID_COMPANY_LOGO_URL_FORMAT)
        }
    }

    private fun validateScheme(uri: URI) {
        val scheme = uri.scheme?.lowercase()
        if (scheme !in ALLOWED_SCHEMES) {
            throw InternshipException(InternshipErrorCode.UNSUPPORTED_COMPANY_LOGO_URL_SCHEME)
        }
    }

    companion object {
        private val ALLOWED_SCHEMES = setOf("http", "https")

        fun from(value: String): CompanyLogoUrl = CompanyLogoUrl(value)
    }
}
