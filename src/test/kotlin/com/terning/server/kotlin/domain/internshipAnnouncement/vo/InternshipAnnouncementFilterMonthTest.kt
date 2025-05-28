package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InternshipAnnouncementFilterMonthTest {
    @Nested
    @DisplayName("from 메서드는")
    inner class From {
        @Test
        @DisplayName("1~12 사이의 값이면 인스턴스를 생성한다")
        fun createMonthSuccessfully() {
            val month = InternshipAnnouncementMonth.from(3)
            assertThat(month.value).isEqualTo(3)
        }

        @Test
        @DisplayName("1 미만 또는 12 초과일 경우 예외를 발생시킨다")
        fun throwExceptionWhenInvalidMonth() {
            val exception =
                assertThrows<InternshipException> {
                    InternshipAnnouncementMonth.from(13)
                }
            assertThat(exception.errorCode).isEqualTo(InternshipErrorCode.INVALID_MONTH)
        }
    }
}
