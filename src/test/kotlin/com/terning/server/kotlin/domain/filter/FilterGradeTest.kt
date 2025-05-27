package com.terning.server.kotlin.domain.filter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class FilterGradeTest {
    @ParameterizedTest(name = "입력: {0} → 기대 결과: {1}")
    @CsvSource(
        "freshman, 1학년",
        "sophomore, 2학년",
        "junior, 3학년",
        "senior, 4학년",
    )
    @DisplayName("올바른 type 문자열을 넣었을 때, 해당 Grade를 반환한다.")
    fun `should return Grade when valid type is provided`(
        type: String,
        expectedLabel: String,
    ) {
        // when
        val filterGrade = FilterGrade.from(type)

        // then
        assertEquals(expectedLabel, filterGrade.label)
    }

    @ParameterizedTest
    @CsvSource("invalid", "fresh", "null", "first")
    @DisplayName("잘못된 type 문자열을 넣었을 때, 예외를 발생시킨다.")
    fun `should throw exception when invalid type is provided`(invalidType: String) {
        // expect
        assertThrows(FilterException::class.java) {
            FilterGrade.from(invalidType)
        }
    }
}
