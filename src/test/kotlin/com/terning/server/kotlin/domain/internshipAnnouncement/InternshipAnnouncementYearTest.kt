package com.terning.server.kotlin.domain.internshipAnnouncement

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InternshipAnnouncementYearTest {
    @Nested
    @DisplayName("from 메서드는")
    inner class From {
        @Test
        @DisplayName("유효한 연도일 경우 인스턴스를 생성한다")
        fun createYearSuccessfully() {
            val year = InternshipAnnouncementYear.from(2025)
            assertThat(year.value).isEqualTo(2025)
        }

        @Test
        @DisplayName("2024 이하일 경우 예외를 발생시킨다")
        fun throwExceptionWhenInvalidYear() {
            val exception =
                assertThrows<InternshipException> {
                    InternshipAnnouncementYear.from(2024)
                }
            assertThat(exception.errorCode).isEqualTo(InternshipErrorCode.INVALID_YEAR)
        }
    }
}
