package com.terning.server.kotlin.domain.internshipAnnouncement

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ScrapCountTest {
    @Test
    @DisplayName("from()을 호출하면 값이 0인 ScrapCount가 생성된다")
    fun `initial scrap count is zero`() {
        // when
        val scrapCount = ScrapCount.from()

        // then
        assertThat(scrapCount.value).isZero()
    }

    @Test
    @DisplayName("increase()를 호출하면 값이 1 증가된 ScrapCount가 반환된다")
    fun `increase returns new instance with incremented value`() {
        // given
        val scrapCount = ScrapCount.from()

        // when
        val increased = scrapCount.increase()

        // then
        assertThat(increased.value).isEqualTo(1)
    }

    @Test
    @DisplayName("decrease()를 호출하면 값이 1 감소된 ScrapCount가 반환된다")
    fun `decrease returns new instance with decremented value`() {
        // given
        val scrapCount = ScrapCount.from().increase()

        // when
        val decreased = scrapCount.decrease()

        // then
        assertThat(decreased.value).isEqualTo(0)
    }

    @Test
    @DisplayName("값이 0일 때 decrease()를 호출하면 예외가 발생한다")
    fun `decrease throws exception when value is zero`() {
        // given
        val scrapCount = ScrapCount.from()

        // when & then
        val exception =
            assertThrows<InternshipException> {
                scrapCount.decrease()
            }

        assertThat(exception.message).isEqualTo("스크랩 수는 0보다 작아질 수 없습니다.")
    }
}
