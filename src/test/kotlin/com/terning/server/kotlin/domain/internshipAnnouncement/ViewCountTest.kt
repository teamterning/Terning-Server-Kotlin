package com.terning.server.kotlin.domain.internshipAnnouncement

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ViewCountTest {
    @Test
    @DisplayName("from()을 호출하면 값이 0인 ViewCount가 생성된다")
    fun `initial view count is zero`() {
        // when
        val viewCount = ViewCount.from()

        // then
        assertThat(viewCount.value).isZero()
    }

    @Test
    @DisplayName("increase()를 호출하면 값이 1 증가된 ViewCount가 반환된다")
    fun `increase returns new instance with incremented value`() {
        // given
        val viewCount = ViewCount.from()

        // when
        val increased = viewCount.increase()

        // then
        assertThat(increased.value).isEqualTo(1)
    }
}
