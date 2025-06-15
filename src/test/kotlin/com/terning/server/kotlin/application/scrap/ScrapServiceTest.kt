package com.terning.server.kotlin.application.scrap

import com.terning.server.kotlin.application.scrap.dto.ScrapRequest
import com.terning.server.kotlin.application.scrap.dto.ScrapUpdateRequest
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncement
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncementRepository
import com.terning.server.kotlin.domain.scrap.Scrap
import com.terning.server.kotlin.domain.scrap.ScrapRepository
import com.terning.server.kotlin.domain.scrap.vo.Color
import com.terning.server.kotlin.domain.user.User
import com.terning.server.kotlin.domain.user.UserRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Optional

class ScrapServiceTest {
    private lateinit var scrapRepository: ScrapRepository
    private lateinit var userRepository: UserRepository
    private lateinit var internshipAnnouncementRepository: InternshipAnnouncementRepository
    private lateinit var scrapService: ScrapService

    private val userId = 1L
    private val announcementId = 100L

    @BeforeEach
    fun setUp() {
        scrapRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        internshipAnnouncementRepository = mockk(relaxed = true)
        scrapService = ScrapService(scrapRepository, userRepository, internshipAnnouncementRepository)
    }

    @Nested
    @DisplayName("스크랩 추가")
    inner class ScrapTest {
        @Test
        @DisplayName("스크랩에 성공한다")
        fun scrapSucceeds() {
            // given
            val user = mockk<User>()
            val announcement = mockk<InternshipAnnouncement>(relaxed = true)
            val scrapSlot = slot<Scrap>()
            val request = ScrapRequest(color = "BLUE")

            givenScrapDoesNotExist()
            givenUserAndAnnouncementExist(user, announcement)
            every { scrapRepository.save(capture(scrapSlot)) } returns mockk()

            // when
            scrapService.scrap(userId, announcementId, request)

            // then
            verify { announcement.increaseScrapCount() }
            assertEquals(Color.BLUE.toHexString(), scrapSlot.captured.hexColor())
        }
    }

    @Nested
    @DisplayName("스크랩 색상 변경")
    inner class UpdateScrapTest {
        @Test
        @DisplayName("스크랩 색상을 성공적으로 업데이트한다")
        fun updateSucceeds() {
            // given
            val scrap = mockk<Scrap>(relaxed = true)
            val updateRequest = ScrapUpdateRequest(color = "RED")

            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns scrap
            every { scrap.updateColor(Color.RED) } just runs
            every { scrapRepository.save(scrap) } returns scrap

            // when
            scrapService.updateScrap(userId, announcementId, updateRequest)

            // then
            verify { scrap.updateColor(Color.RED) }
            verify { scrapRepository.save(scrap) }
        }
    }

    private fun givenScrapDoesNotExist() {
        every { scrapRepository.existsByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns false
    }

    private fun givenAnnouncementExists(announcement: InternshipAnnouncement) {
        every { internshipAnnouncementRepository.findById(announcementId) } returns Optional.of(announcement)
    }

    private fun givenUserAndAnnouncementExist(
        user: User,
        announcement: InternshipAnnouncement,
    ) {
        givenAnnouncementExists(announcement)
        every { userRepository.findById(userId) } returns Optional.of(user)
    }
}
