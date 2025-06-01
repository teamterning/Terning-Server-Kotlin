package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class InternshipAnnouncementDeadlineTest {
    @Test
    @DisplayName("마감일이 오늘보다 이전이면 isOver는 true를 반환한다")
    fun `deadline before today returns true`() {
        val internshipAnnouncementDeadline = InternshipAnnouncementDeadline.from(LocalDate.now())
        val result = internshipAnnouncementDeadline.isOver(LocalDate.now().plusDays(1))
        assertThat(result).isTrue
    }

    @Test
    @DisplayName("마감일이 오늘이면 isOver는 false를 반환한다")
    fun `deadline equals today returns false`() {
        val today = LocalDate.now()
        val internshipAnnouncementDeadline = InternshipAnnouncementDeadline.from(today.plusDays(1))
        val result = internshipAnnouncementDeadline.isOver(today.plusDays(1))
        assertThat(result).isFalse
    }

    @Test
    @DisplayName("마감일이 오늘보다 이후면 isOver는 false를 반환한다")
    fun `deadline after today returns false`() {
        val internshipAnnouncementDeadline = InternshipAnnouncementDeadline.from(LocalDate.now().plusDays(2))
        val result = internshipAnnouncementDeadline.isOver()
        assertThat(result).isFalse
    }

    @Test
    @DisplayName("마감일이 2024년 1월 1일보다 이전이면 예외가 발생한다")
    fun `deadline before 2024-01-02 throws exception`() {
        val invalid = LocalDate.of(2024, 1, 1)

        assertThatThrownBy { InternshipAnnouncementDeadline.from(invalid) }
            .isInstanceOf(InternshipAnnouncementException::class.java)
            .hasMessage(InternshipAnnouncementErrorCode.INVALID_DEADLINE.message)
    }

    @Test
    @DisplayName("마감일이 2024년 1월 2일이면 생성된다")
    fun `deadline after 2024-01-01 is valid`() {
        val valid = LocalDate.of(2024, 1, 2)
        val result = InternshipAnnouncementDeadline.from(valid)
        assertThat(result.value).isEqualTo(valid)
    }
}
