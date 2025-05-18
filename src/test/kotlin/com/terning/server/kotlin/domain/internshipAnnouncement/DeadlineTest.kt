package com.terning.server.kotlin.domain.internshipAnnouncement

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DeadlineTest {
    @Test
    @DisplayName("마감일이 오늘보다 이전이면 isOver는 true를 반환한다")
    fun `deadline before today returns true`() {
        // given
        val yesterday = LocalDate.now().minusDays(1)
        val deadline = Deadline.from(yesterday)

        // when
        val result = deadline.isOver()

        // then
        assertThat(result).isTrue
    }

    @Test
    @DisplayName("마감일이 오늘이면 isOver는 false를 반환한다")
    fun `deadline equals today returns false`() {
        // given
        val today = LocalDate.now()
        val deadline = Deadline.from(today)

        // when
        val result = deadline.isOver()

        // then
        assertThat(result).isFalse
    }

    @Test
    @DisplayName("마감일이 오늘보다 이후면 isOver는 false를 반환한다")
    fun `deadline after today returns false`() {
        // given
        val tomorrow = LocalDate.now().plusDays(1)
        val deadline = Deadline.from(tomorrow)

        // when
        val result = deadline.isOver()

        // then
        assertThat(result).isFalse
    }

    @DisplayName("마감일이 2025년 1월 2일보다 이전이면 예외가 발생한다")
    fun `deadline before 2025-01-02 throws exception`() {
        // given
        val invalid = LocalDate.of(2025, 1, 1)

        // when
        val result = runCatching { Deadline.from(invalid) }

        // then
        assertThat(result.exceptionOrNull())
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("마감일은 2025년 이후여야 합니다.")
    }

    @DisplayName("마감일이 2025년 1월 2일이면 생성된다")
    fun `deadline after 2025-01-01 is valid`() {
        // given
        val valid = LocalDate.of(2025, 1, 2)

        // when
        val result = Deadline.from(valid)

        // then
        assertThat(result.value).isEqualTo(valid)
    }
}
