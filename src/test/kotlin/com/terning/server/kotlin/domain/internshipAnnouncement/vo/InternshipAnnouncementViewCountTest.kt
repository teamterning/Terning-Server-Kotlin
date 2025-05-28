package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class InternshipAnnouncementViewCountTest {
    @Test
    @DisplayName("from()을 호출하면 값이 0인 ViewCount가 생성된다")
    fun `initial view count is zero`() {
        // when
        val internshipAnnouncementViewCount = InternshipAnnouncementViewCount.from()

        // then
        assertThat(internshipAnnouncementViewCount.value).isZero()
    }

    @Test
    @DisplayName("increase()를 호출하면 값이 1 증가된 ViewCount가 반환된다")
    fun `increase returns new instance with incremented value`() {
        // given
        val internshipAnnouncementViewCount = InternshipAnnouncementViewCount.from()

        // when
        val increased = internshipAnnouncementViewCount.increase()

        // then
        assertThat(increased.value).isEqualTo(1)
    }
}
