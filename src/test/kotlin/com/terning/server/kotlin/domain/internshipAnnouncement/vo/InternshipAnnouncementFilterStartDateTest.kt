package com.terning.server.kotlin.domain.internshipAnnouncement.vo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class InternshipAnnouncementFilterStartDateTest {
    @Test
    @DisplayName("유효한 연도와 월이 주어지면 StartDate를 생성한다")
    fun createStartDateSuccessfully() {
        val year = InternshipAnnouncementYear.from(2026)
        val month = InternshipAnnouncementMonth.from(5)
        val startDate = InternshipAnnouncementStartDate.of(year, month)

        assertThat(startDate.year).isEqualTo(year)
        assertThat(startDate.month).isEqualTo(month)
        assertThat(startDate.toString()).isEqualTo("2026년 5월")
    }

    @Test
    @DisplayName("같은 연도와 월이면 equals는 true를 반환한다")
    fun returnTrueWhenSameYearAndMonth() {
        val year = InternshipAnnouncementYear.from(2026)
        val month = InternshipAnnouncementMonth.from(5)

        val startDate1 = InternshipAnnouncementStartDate.of(year, month)
        val startDate2 = InternshipAnnouncementStartDate.of(year, month)

        assertThat(startDate1).isEqualTo(startDate2)
        assertThat(startDate1.hashCode()).isEqualTo(startDate2.hashCode())
    }
}
