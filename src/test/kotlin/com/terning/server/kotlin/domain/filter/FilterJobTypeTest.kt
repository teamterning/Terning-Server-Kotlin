package com.terning.server.kotlin.domain.filter

import com.terning.server.kotlin.domain.scrap.ScrapException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class FilterJobTypeTest {
    @ParameterizedTest(name = "입력: {0} → 기대 결과: {1}")
    @CsvSource(
        "total, 전체",
        "plan, 기획/전략",
        "marketing, 마케팅/홍보",
        "admin, 사무/회계",
        "sales, 인사/영업",
        "design, 디자인/예술",
        "it, 개발/IT",
        "research, 연구/생산",
        "etc, 기타",
    )
    @DisplayName("올바른 type 문자열을 넣었을 때, 해당 JobType을 반환한다.")
    fun `should return JobType when valid type is provided`(
        type: String,
        expectedLabel: String,
    ) {
        // when
        val filterJobType = FilterJobType.from(type)

        // then
        assertEquals(expectedLabel, filterJobType.label)
    }

    @ParameterizedTest
    @CsvSource("invalid", "unknown", "none", "test")
    @DisplayName("잘못된 type 문자열을 넣었을 때, 예외를 발생시킨다.")
    fun `should throw exception when invalid type is provided`(invalidType: String) {
        // expect
        assertThrows(ScrapException::class.java) {
            FilterJobType.from(invalidType)
        }
    }
}
