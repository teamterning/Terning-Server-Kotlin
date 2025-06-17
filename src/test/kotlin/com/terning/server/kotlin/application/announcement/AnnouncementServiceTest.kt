package com.terning.server.kotlin.application.announcement

import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncement
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncementRepository
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementErrorCode
import com.terning.server.kotlin.domain.internshipAnnouncement.exception.InternshipAnnouncementException
import com.terning.server.kotlin.domain.scrap.Scrap
import com.terning.server.kotlin.domain.scrap.ScrapRepository
import com.terning.server.kotlin.domain.user.User
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Optional

class AnnouncementServiceTest {
    private lateinit var internshipRepository: InternshipAnnouncementRepository
    private lateinit var service: AnnouncementService
    private lateinit var scrapRepository: ScrapRepository
    private lateinit var clock: Clock
    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        internshipRepository = mockk()
        user = mockk()
        scrapRepository = mockk()
        clock = Clock.fixed(Instant.parse("2025-06-08T00:00:00Z"), ZoneId.systemDefault())
        service =
            AnnouncementService(
                internshipRepository = internshipRepository,
                scrapRepository = scrapRepository,
                clock = clock,
            )
    }

    @Nested
    @DisplayName("getDetailAnnouncement 메소드는")
    inner class GetDetailAnnouncement {
        @Test
        @DisplayName("공고를 찾을 수 없으면 예외를 던진다")
        fun `it throws exception when internship not found`() {
            // given
            val internshipId = 1L
            every { internshipRepository.findById(internshipId) } returns Optional.empty()

            // when & then
            val exception =
                assertThrows<InternshipAnnouncementException> {
                    service.getDetailAnnouncement(userId = 1L, internshipAnnouncementId = internshipId)
                }
            Assertions.assertEquals(InternshipAnnouncementErrorCode.NOT_FOUND_ANNOUNCEMENT_EXCEPTION, exception.errorCode)
        }

        @Test
        @DisplayName("스크랩 정보가 없으면 isScrapped=false, color=null을 반환한다")
        fun `it returns non-scrapped info if no scrap found`() {
            // given
            val internship =
                mockk<InternshipAnnouncement> {
                    every { company.logoUrl.value } returns "https://media-cdn.linkareer.com/activity_manager/logos/467093"
                    every { internshipAnnouncementDeadline.value } returns LocalDate.of(2025, 7, 1)
                    every { title.value } returns "[카카오페이] 가맹점 업무 지원 어시스턴트 채용"
                    every { workingPeriod.value } returns 3
                    every { startDate.year.value } returns 2025
                    every { startDate.month.value } returns 8
                    every { internshipAnnouncementScrapCount.value } returns 0
                    every { internshipAnnouncementViewCount.value } returns 1
                    every { company.name.value } returns "카카오페이"
                    every { company.category.displayName } returns "대기엽/중견기업"
                    every { qualifications } returns "졸업 예정자, 휴학생 가능"
                    every { filterJobType.type } returns "IT"
                    every { detail } returns "[지원자격] 성실하고 꼼꼼한 성격을 소유하신 분이면 좋겠어요."
                    every { url.value } returns "https://kakaopay.career.greetinghr.com/ko/o/136662"
                    every { increaseViewCount() } returns Unit
                }

            every { internshipRepository.findById(any()) } returns Optional.of(internship)
            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(any(), any()) } returns null

            // when
            val result = service.getDetailAnnouncement(userId = 1L, internshipAnnouncementId = 1L)

            // then
            Assertions.assertFalse(result.isScrapped)
            Assertions.assertNull(result.color)
        }

        @Test
        @DisplayName("스크랩 정보가 있으면 isScrapped=true, color이 함께 반환한다")
        fun `it returns scrapped info if scrap found`() {
            // given
            val internship =
                mockk<InternshipAnnouncement> {
                    every { company.logoUrl.value } returns "https://media-cdn.linkareer.com/activity_manager/logos/467093"
                    every { internshipAnnouncementDeadline.value } returns LocalDate.of(2025, 7, 1)
                    every { title.value } returns "[카카오페이] 가맹점 업무 지원 어시스턴트 채용"
                    every { workingPeriod.value } returns 6
                    every { startDate.year.value } returns 2025
                    every { startDate.month.value } returns 8
                    every { internshipAnnouncementScrapCount.value } returns 2
                    every { internshipAnnouncementViewCount.value } returns 5
                    every { company.name.value } returns "카카오페이"
                    every { company.category.displayName } returns "대기엽/중견기업"
                    every { qualifications } returns "졸업 예정자, 휴학생 가능"
                    every { filterJobType.type } returns "IT"
                    every { detail } returns "[지원자격] 성실하고 꼼꼼한 성격을 소유하신 분이면 좋겠어요."
                    every { url.value } returns "https://kakaopay.career.greetinghr.com/ko/o/136662"
                    every { increaseViewCount() } returns Unit
                }

            val scrap =
                mockk<Scrap> {
                    every { hexColor() } returns "#F3A649"
                }

            every { internshipRepository.findById(any()) } returns Optional.of(internship)
            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(any(), any()) } returns scrap

            // when
            val result = service.getDetailAnnouncement(userId = 1L, internshipAnnouncementId = 1L)

            // then
            Assertions.assertTrue(result.isScrapped)
            Assertions.assertEquals("#F3A649", result.color)
        }
    }
}
