package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CompanyLogoUrlTest {
    @Nested
    @DisplayName("from 메서드는")
    inner class From {
        @Test
        @DisplayName("http 또는 https로 시작하는 유효한 URL이면 인스턴스를 생성한다")
        fun createLogoUrlSuccessfully() {
            val url = "https://example.com/logo.png"
            val logoUrl = CompanyLogoUrl.from(url)

            assertThat(logoUrl.value).isEqualTo(url)
            assertThat(logoUrl.toString()).isEqualTo(url)
        }

        @Test
        @DisplayName("지원하지 않는 scheme이면 예외를 발생시킨다 (ftp 등)")
        fun throwExceptionForUnsupportedScheme() {
            val invalidSchemeUrl = "ftp://example.com/logo.png"

            val exception =
                assertThrows<InternshipAnnouncementException> {
                    CompanyLogoUrl.from(invalidSchemeUrl)
                }

            assertThat(exception.errorCode)
                .isEqualTo(InternshipAnnouncementErrorCode.UNSUPPORTED_COMPANY_LOGO_URL_SCHEME)
        }

        @Test
        @DisplayName("URL 형식이 잘못된 경우 예외를 발생시킨다")
        fun throwExceptionForMalformedUrl() {
            val malformedUrl = "://not-a-valid-url"

            val exception =
                assertThrows<InternshipAnnouncementException> {
                    CompanyLogoUrl.from(malformedUrl)
                }

            assertThat(exception.errorCode)
                .isEqualTo(InternshipAnnouncementErrorCode.INVALID_COMPANY_LOGO_URL_FORMAT)
        }
    }
}
