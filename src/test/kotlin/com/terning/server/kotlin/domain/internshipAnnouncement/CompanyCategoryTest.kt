package com.terning.server.kotlin.domain.internshipAnnouncement

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class CompanyCategoryTest {
    @DisplayName("유효한 categoryId를 전달하면 해당 CompanyCategory를 반환한다")
    @ParameterizedTest(name = "categoryId {0} -> {1}")
    @CsvSource(
        "0, LARGE_AND_MEDIUM_COMPANIES",
        "1, SMALL_COMPANIES",
        "2, PUBLIC_INSTITUTIONS",
        "3, FOREIGN_COMPANIES",
        "4, STARTUPS",
        "5, NON_PROFIT_ORGANIZATIONS",
        "6, OTHERS",
    )
    fun `return correct CompanyCategory for valid categoryId`(
        categoryId: Int,
        expectedEnumName: String,
    ) {
        // when
        val result = CompanyCategory.from(categoryId)
        // then
        assertThat(result.name).isEqualTo(expectedEnumName)
    }

    @DisplayName("유효하지 않은 categoryId를 전달하면 InternshipException을 던진다")
    @ParameterizedTest(name = "categoryId {0} -> exception")
    @ValueSource(ints = [-1, 7, 100, 999])
    fun `throw exception when categoryId is invalid`(invalidCategoryId: Int) {
        // when & then
        val exception =
            assertThrows<InternshipException> {
                CompanyCategory.from(invalidCategoryId)
            }
        assertThat(exception.errorCode).isEqualTo(InternshipErrorCode.INVALID_COMPANY_CATEGORY)
    }
}
