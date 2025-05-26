package com.terning.server.kotlin.domain.internshipAnnouncement

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class CompanyCategoryTest {
    @DisplayName("displayName으로 CompanyCategory를 조회할 수 있다")
    @ParameterizedTest(name = "displayName: {0} -> {1}")
    @CsvSource(
        "대기업/중견기업, LARGE_AND_MEDIUM_COMPANIES",
        "중소기업, SMALL_COMPANIES",
        "공공기관/공기업, PUBLIC_INSTITUTIONS",
        "외국계기업, FOREIGN_COMPANIES",
        "스타트업, STARTUPS",
        "비영리단체/재단, NON_PROFIT_ORGANIZATIONS",
        "기타, OTHERS",
    )
    fun `return correct CompanyCategory for valid displayName`(
        displayName: String,
        expectedEnumName: String,
    ) {
        val result = CompanyCategory.from(displayName)
        assertThat(result.name).isEqualTo(expectedEnumName)
    }

    @DisplayName("존재하지 않는 displayName을 전달하면 예외가 발생한다")
    @ParameterizedTest(name = "invalid displayName: {0}")
    @ValueSource(strings = ["", "대기업", "터닝", "UNKNOWN", "스타트업스"])
    fun `throw exception when displayName is invalid`(invalidName: String) {
        val exception =
            assertThrows<InternshipException> {
                CompanyCategory.from(invalidName)
            }
        assertThat(exception.errorCode).isEqualTo(InternshipErrorCode.INVALID_COMPANY_CATEGORY)
    }
}
