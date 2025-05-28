package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CompanyNameTest {
    @Nested
    @DisplayName("CompanyName.from 메서드는")
    inner class From {
        @Test
        @DisplayName("유효한 값이면 CompanyName을 생성한다")
        fun `create CompanyName when valid`() {
            // given
            val value = "구글"

            // when
            val result = CompanyName.from(value)

            // then
            assertThat(result.value).isEqualTo(value)
        }

        @Test
        @DisplayName("비어 있거나 공백뿐인 값이면 예외를 던진다")
        fun `throw exception when name is blank`() {
            // given
            val blank = "    "

            // when & then
            val exception =
                assertThrows<InternshipException> {
                    CompanyName.from(blank)
                }

            assertThat(exception.errorCode).isEqualTo(InternshipErrorCode.INVALID_COMPANY_NAME_EMPTY)
        }

        @Test
        @DisplayName("64자를 초과하는 값이면 예외를 던진다")
        fun `throw exception when name is too long`() {
            // given
            val tooLong = "A".repeat(65)

            // when & then
            val exception =
                assertThrows<InternshipException> {
                    CompanyName.from(tooLong)
                }

            assertThat(exception.errorCode).isEqualTo(InternshipErrorCode.INVALID_COMPANY_NAME_TOO_LONG)
        }
    }
}
