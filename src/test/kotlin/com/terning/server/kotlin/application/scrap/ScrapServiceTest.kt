package com.terning.server.kotlin.application

import com.terning.server.kotlin.application.scrap.dto.ScrapRequest
import com.terning.server.kotlin.application.scrap.dto.ScrapUpdateRequest
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncement
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncementRepository
import com.terning.server.kotlin.domain.scrap.Scrap
import com.terning.server.kotlin.domain.scrap.ScrapRepository
import com.terning.server.kotlin.domain.scrap.exception.ScrapErrorCode
import com.terning.server.kotlin.domain.scrap.exception.ScrapException
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
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.Optional

class ScrapServiceTest {
    private val scrapRepository: ScrapRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)
    private val internshipAnnouncementRepository: InternshipAnnouncementRepository = mockk(relaxed = true)
    private lateinit var scrapService: ScrapService

    private val userId = 1L
    private val announcementId = 100L
    private val request = ScrapRequest(color = "BLUE")

    @BeforeEach
    fun setUp() {
        val clock = Clock.fixed(Instant.parse("2025-06-08T00:00:00Z"), ZoneId.systemDefault())
        scrapService = ScrapService(scrapRepository, userRepository, internshipAnnouncementRepository, clock)
    }

    @Nested
    @DisplayName("스크랩 추가")
    inner class ScrapTest {
        @Test
        @DisplayName("이미 스크랩한 경우 예외가 발생한다")
        fun scrapFailsIfAlreadyScrapped() {
            // given
            givenScrapExists()

            // when & then
            val exception = assertThrows<ScrapException> { scrapService.scrap(userId, announcementId, request) }
            assertEquals(ScrapErrorCode.EXISTS_SCRAP_ALREADY, exception.errorCode)
        }

        @Test
        @DisplayName("공고를 찾을 수 없으면 예외가 발생한다")
        fun scrapFailsIfAnnouncementNotFound() {
            // given
            givenScrapDoesNotExist()
            every { internshipAnnouncementRepository.findById(announcementId) } returns Optional.empty()

            // when & then
            val exception = assertThrows<ScrapException> { scrapService.scrap(userId, announcementId, request) }
            assertEquals(ScrapErrorCode.INTERN_SHIP_ANNOUNCEMENT_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("사용자를 찾을 수 없으면 예외가 발생한다")
        fun scrapFailsIfUserNotFound() {
            // given
            givenScrapDoesNotExist()
            givenAnnouncementExists(mockk())
            every { userRepository.findById(userId) } returns Optional.empty()

            // when & then
            val exception = assertThrows<ScrapException> { scrapService.scrap(userId, announcementId, request) }
            assertEquals(ScrapErrorCode.USER_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("스크랩에 성공한다")
        fun scrapSucceeds() {
            // given
            val user = mockk<User>()
            val announcement = mockk<InternshipAnnouncement>(relaxed = true)
            val scrapSlot = slot<Scrap>()

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
        private val updateRequest = ScrapUpdateRequest(color = "RED")
        private val scrap = mockk<Scrap>(relaxed = true)

        @Test
        @DisplayName("스크랩이 존재하지 않으면 예외가 발생한다")
        fun updateFailsIfScrapNotFound() {
            // given
            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns null

            // when & then
            val exception = assertThrows<ScrapException> { scrapService.updateScrap(userId, announcementId, updateRequest) }
            assertEquals(ScrapErrorCode.SCRAP_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("스크랩 색상을 성공적으로 업데이트한다")
        fun updateSucceeds() {
            // given
            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns scrap
            every { scrap.updateColor(Color.RED) } just runs
            // [수정] 누락되었던 save() mocking 추가
            every { scrapRepository.save(scrap) } returns scrap

            // when
            scrapService.updateScrap(userId, announcementId, updateRequest)

            // then
            verify { scrap.updateColor(Color.RED) }
            verify { scrapRepository.save(scrap) }
        }
    }

    @Nested
    @DisplayName("스크랩 취소")
    inner class CancelScrapTest {
        private val scrap = mockk<Scrap>()
        private val announcement = mockk<InternshipAnnouncement>(relaxed = true)

        @Test
        @DisplayName("스크랩이 존재하지 않으면 예외가 발생한다")
        fun cancelFailsIfScrapNotFound() {
            // given
            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns null

            // when & then
            val exception = assertThrows<ScrapException> { scrapService.cancelScrap(userId, announcementId) }
            assertEquals(ScrapErrorCode.SCRAP_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("공고가 존재하지 않으면 예외가 발생한다")
        fun cancelFailsIfAnnouncementNotFound() {
            // given
            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns scrap
            every { internshipAnnouncementRepository.findById(announcementId) } returns Optional.empty()

            // when & then
            val exception = assertThrows<ScrapException> { scrapService.cancelScrap(userId, announcementId) }
            assertEquals(ScrapErrorCode.INTERN_SHIP_ANNOUNCEMENT_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("스크랩 취소에 성공한다")
        fun cancelSucceeds() {
            // given
            every { scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns scrap
            every { internshipAnnouncementRepository.findById(announcementId) } returns Optional.of(announcement)
            every { scrapRepository.delete(scrap) } just runs

            // when
            scrapService.cancelScrap(userId, announcementId)

            // then
            verify { announcement.decreaseScrapCount() }
            verify { scrapRepository.delete(scrap) }
        }
    }

    private fun givenScrapExists() {
        every { scrapRepository.existsByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns true
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
