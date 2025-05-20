package com.terning.server.kotlin.domain.internshipAnnouncement

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InternshipAnnouncementUrlTest {
    @Nested
    @DisplayName("from 메서드는")
    inner class From {
        @Test
        @DisplayName("http로 시작하는 유효한 URL이면 인스턴스를 생성한다")
        fun createAnnouncementUrlSuccessfully() {
            val url = "https://example.com/post/123"
            val announcementUrl = InternshipAnnouncementUrl.from(url)

            assertThat(announcementUrl.value).isEqualTo(url)
            assertThat(announcementUrl.toString()).isEqualTo(url)
        }

        @Test
        @DisplayName("http로 시작하지 않으면 예외를 발생시킨다")
        fun throwExceptionWhenInvalidUrl() {
            val invalidUrl = "ftp://example.com/post/123"

            val exception =
                assertThrows<InternshipException> {
                    InternshipAnnouncementUrl.from(invalidUrl)
                }

            assertThat(exception.errorCode).isEqualTo(InternshipErrorCode.INVALID_ANNOUNCEMENT_URL)
        }
    }
}
