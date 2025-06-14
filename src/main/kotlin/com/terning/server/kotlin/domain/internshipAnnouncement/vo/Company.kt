package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
class Company private constructor(
    @Embedded
    val name: CompanyName,

    @Enumerated(EnumType.STRING)
    val category: CompanyCategory,

    @Embedded
    val logoUrl: CompanyLogoUrl,
) {
    override fun equals(other: Any?): Boolean =
        this === other || (
            other is Company &&
                name == other.name &&
                category == other.category &&
                logoUrl == other.logoUrl
        )

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + logoUrl.hashCode()
        return result
    }

    override fun toString(): String = "${name.value} (${category.displayName})"

    companion object {
        fun of(
            name: CompanyName,
            category: CompanyCategory,
            logoUrl: CompanyLogoUrl,
        ): Company = Company(name, category, logoUrl)
    }
}
