package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InternshipTitleTest {
    @Nested
    @DisplayName("InternshipTitle.from 메서드는")
    inner class From {
        @Test
        @DisplayName("유효한 값이면 InternshipTitle을 생성한다")
        fun `create InternshipTitle when valid`() {
            // given
            val value = "백엔드 인턴십"

            // when
            val result = InternshipTitle.from(value)

            // then
            assertThat(result.value).isEqualTo(value)
        }

        @Test
        @DisplayName("비어 있거나 공백뿐인 값이면 예외를 던진다")
        fun `throw exception when title is blank`() {
            // given
            val blank = "    "

            // when & then
            val exception =
                assertThrows<InternshipException> {
                    InternshipTitle.from(blank)
                }

            assertThat(exception.errorCode).isEqualTo(InternshipErrorCode.INVALID_INTERNSHIP_TITLE_EMPTY)
        }

        @Test
        @DisplayName("64자를 초과하는 값이면 예외를 던진다")
        fun `throw exception when title is too long`() {
            // given
            val tooLong = "인턴십".repeat(22)

            // when & then
            val exception =
                assertThrows<InternshipException> {
                    InternshipTitle.from(tooLong)
                }

            assertThat(exception.errorCode).isEqualTo(InternshipErrorCode.INVALID_INTERNSHIP_TITLE_TOO_LONG)
        }
    }
}
