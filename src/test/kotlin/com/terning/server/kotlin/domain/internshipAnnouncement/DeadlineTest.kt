package com.terning.server.kotlin.domain.internshipAnnouncement

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DeadlineTest {
    @Test
    @DisplayName("마감일이 오늘보다 이전이면 isOver는 true를 반환한다")
    fun `deadline before today returns true`() {
        val deadline = Deadline.from(LocalDate.now())
        val result = deadline.isOver(LocalDate.now().plusDays(1))
        assertThat(result).isTrue
    }

    @Test
    @DisplayName("마감일이 오늘이면 isOver는 false를 반환한다")
    fun `deadline equals today returns false`() {
        val today = LocalDate.now()
        val deadline = Deadline.from(today.plusDays(1))
        val result = deadline.isOver(today.plusDays(1))
        assertThat(result).isFalse
    }

    @Test
    @DisplayName("마감일이 오늘보다 이후면 isOver는 false를 반환한다")
    fun `deadline after today returns false`() {
        val deadline = Deadline.from(LocalDate.now().plusDays(2))
        val result = deadline.isOver()
        assertThat(result).isFalse
    }

    @Test
    @DisplayName("마감일이 2024년 1월 1일보다 이전이면 예외가 발생한다")
    fun `deadline before 2024-01-02 throws exception`() {
        val invalid = LocalDate.of(2024, 1, 1)

        assertThatThrownBy { Deadline.from(invalid) }
            .isInstanceOf(InternshipException::class.java)
            .hasMessage(InternshipErrorCode.INVALID_DEADLINE.message)
    }

    @Test
    @DisplayName("마감일이 2024년 1월 2일이면 생성된다")
    fun `deadline after 2024-01-01 is valid`() {
        val valid = LocalDate.of(2024, 1, 2)
        val result = Deadline.from(valid)
        assertThat(result.value).isEqualTo(valid)
    }
}
