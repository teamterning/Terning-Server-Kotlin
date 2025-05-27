package com.terning.server.kotlin.domain.internshipAnnouncement

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CompanyTest {
    @Nested
    @DisplayName("Company.of 메서드는")
    inner class Of {
        @Test
        @DisplayName("CompanyName, CompanyCategory, CompanyLogoUrl로 Company를 생성한다")
        fun createCompanySuccessfully() {
            // given
            val name = CompanyName.from("카카오페이")
            val category = CompanyCategory.LARGE_AND_MEDIUM_COMPANIES
            val logoUrl = CompanyLogoUrl.from("https://example.com/logo.png")

            // when
            val company = Company.of(name, category, logoUrl)

            // then
            assertThat(company.name).isEqualTo(name)
            assertThat(company.category).isEqualTo(category)
            assertThat(company.logoUrl).isEqualTo(logoUrl)
        }
    }

    @Test
    @DisplayName("toString은 '회사명 (설명)' 형식으로 출력한다")
    fun toStringShouldFormatCorrectly() {
        // given
        val name = CompanyName.from("카카오페이")
        val category = CompanyCategory.LARGE_AND_MEDIUM_COMPANIES
        val logoUrl = CompanyLogoUrl.from("https://example.com/logo.png")

        val company = Company.of(name, category, logoUrl)

        // then
        assertThat(company.toString()).isEqualTo("카카오페이 (대기업/중견기업)")
    }
}
