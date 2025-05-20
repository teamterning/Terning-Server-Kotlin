package com.terning.server.kotlin.domain.internshipAnnouncement

import com.terning.server.kotlin.domain.filter.JobType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class InternshipAnnouncementTest {
    @Test
    @DisplayName("조회수를 증가시키면 viewCount가 1 증가한다")
    fun increaseViewCount_updatesCorrectly() {
        // given
        val announcement = createSampleAnnouncement()

        // when
        announcement.increaseViewCount()

        // then
        assertThat(announcement.viewCount.value).isEqualTo(1)
    }

    @Test
    @DisplayName("스크랩 수를 증가시키면 scrapCount가 1 증가한다")
    fun increaseScrapCount_updatesCorrectly() {
        // given
        val announcement = createSampleAnnouncement()

        // when
        announcement.increaseScrapCount()

        // then
        assertThat(announcement.scrapCount.value).isEqualTo(1)
    }

    @Test
    @DisplayName("스크랩 수를 감소시키면 scrapCount가 1 감소한다")
    fun decreaseScrapCount_updatesCorrectly() {
        // given
        val announcement =
            createSampleAnnouncement().apply {
                increaseScrapCount()
            }

        // when
        announcement.decreaseScrapCount()

        // then
        assertThat(announcement.scrapCount.value).isEqualTo(0)
    }

    @Test
    @DisplayName("스크랩 수가 0일 때 감소시키면 예외가 발생한다")
    fun decreaseScrapCount_throwsExceptionWhenAlreadyZero() {
        // given
        val announcement = createSampleAnnouncement()

        // expect
        assertThrows(InternshipException::class.java) {
            announcement.decreaseScrapCount()
        }
    }

    private fun createSampleAnnouncement(): InternshipAnnouncement {
        return InternshipAnnouncement(
            title = InternshipTitle.from("카카오 인턴 모집"),
            deadline = Deadline.from(LocalDate.now().plusDays(7)),
            workingPeriod = InternshipWorkingPeriod.from(3),
            startDate =
                InternshipAnnouncementStartDate.of(
                    InternshipAnnouncementYear.from(2025),
                    InternshipAnnouncementMonth.from(6),
                ),
            url = InternshipAnnouncementUrl.from("https://example.com"),
            company =
                Company.of(
                    CompanyName.from("카카오"),
                    CompanyCategory.LARGE_AND_MEDIUM_COMPANIES,
                    CompanyLogoUrl.from("https://logo.com/kakao.png"),
                ),
            jobType = JobType.IT,
            isGraduating = true,
        )
    }
}
