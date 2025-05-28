package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipException
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
        @DisplayName("http 또는 https로 시작하는 유효한 URL이면 인스턴스를 생성한다")
        fun createAnnouncementUrlSuccessfully() {
            val url = "https://example.com/post/123"
            val announcementUrl = InternshipAnnouncementUrl.from(url)

            assertThat(announcementUrl.value).isEqualTo(url)
            assertThat(announcementUrl.toString()).isEqualTo(url)
        }

        @Test
        @DisplayName("지원하지 않는 scheme이면 예외를 발생시킨다 (ftp 등)")
        fun throwExceptionForUnsupportedScheme() {
            val invalidSchemeUrl = "ftp://example.com/post/123"

            val exception =
                assertThrows<InternshipException> {
                    InternshipAnnouncementUrl.from(invalidSchemeUrl)
                }

            assertThat(exception.errorCode)
                .isEqualTo(InternshipErrorCode.UNSUPPORTED_ANNOUNCEMENT_URL_SCHEME)
        }

        @Test
        @DisplayName("URL 형식이 잘못된 경우 예외를 발생시킨다")
        fun throwExceptionForMalformedUrl() {
            val malformedUrl = "://invalid-url"

            val exception =
                assertThrows<InternshipException> {
                    InternshipAnnouncementUrl.from(malformedUrl)
                }

            assertThat(exception.errorCode)
                .isEqualTo(InternshipErrorCode.INVALID_ANNOUNCEMENT_URL_FORMAT)
        }
    }
}
