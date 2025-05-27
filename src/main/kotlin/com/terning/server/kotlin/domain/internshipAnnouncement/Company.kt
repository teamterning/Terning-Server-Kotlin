package com.terning.server.kotlin.domain.internshipAnnouncement

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
class Company private constructor(
    @Embedded
    @Column(name = "companyInfo")
    val name: CompanyName,
    @Enumerated(EnumType.STRING)
    @Column(name = "companyCategory")
    val category: CompanyCategory,
    @Embedded
    @Column(name = "companyImage")
    val logoUrl: CompanyLogoUrl,
) {
    companion object {
        fun of(
            name: CompanyName,
            category: CompanyCategory,
            logoUrl: CompanyLogoUrl,
        ): Company = Company(name, category, logoUrl)
    }

    override fun equals(other: Any?): Boolean {
        return this === other || (
            other is Company &&
                name == other.name &&
                category == other.category &&
                logoUrl == other.logoUrl
        )
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + logoUrl.hashCode()
        return result
    }

    override fun toString(): String = "${name.value} (${category.displayName})"
}
