package com.terning.server.kotlin.application.scrap

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
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Optional

class ScrapServiceTest {
    private val scrapRepository: ScrapRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val internshipAnnouncementRepository: InternshipAnnouncementRepository = mockk()

    private lateinit var scrapService: ScrapService

    private val userId = 1L
    private val announcementId = 100L
    private val request = ScrapRequest(color = "BLUE")

    @BeforeEach
    fun setUp() {
        scrapService = ScrapService(scrapRepository, userRepository, internshipAnnouncementRepository)
    }

    @Nested
    @DisplayName("스크랩 추가")
    inner class ScrapTest {
        @Test
        @DisplayName("이미 스크랩한 경우 예외가 발생한다")
        fun scrapFailsIfAlreadyScrapped() {
            every { scrapRepository.existsByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns true

            val exception =
                assertThrows(ScrapException::class.java) {
                    scrapService.scrap(userId, announcementId, request)
                }

            assertEquals(ScrapErrorCode.EXISTS_SCRAP_ALREADY, exception.errorCode)
        }

        @Test
        @DisplayName("공고를 찾을 수 없으면 예외가 발생한다")
        fun scrapFailsIfAnnouncementNotFound() {
            every { scrapRepository.existsByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns false
            every { internshipAnnouncementRepository.findById(announcementId) } returns Optional.empty()

            val exception =
                assertThrows(ScrapException::class.java) {
                    scrapService.scrap(userId, announcementId, request)
                }

            assertEquals(ScrapErrorCode.INTERN_SHIP_ANNOUNCEMENT_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("사용자를 찾을 수 없으면 예외가 발생한다")
        fun scrapFailsIfUserNotFound() {
            every { scrapRepository.existsByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns false
            every { internshipAnnouncementRepository.findById(announcementId) } returns Optional.of(mockk())
            every { userRepository.findById(userId) } returns Optional.empty()

            val exception =
                assertThrows(ScrapException::class.java) {
                    scrapService.scrap(userId, announcementId, request)
                }

            assertEquals(ScrapErrorCode.USER_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("스크랩에 성공한다")
        fun scrapSucceeds() {
            val user = mockk<User>()
            val announcement = mockk<InternshipAnnouncement>(relaxed = true)
            val scrapSlot = slot<Scrap>()

            every { scrapRepository.existsByUserIdAndInternshipAnnouncementId(userId, announcementId) } returns false
            every { internshipAnnouncementRepository.findById(announcementId) } returns Optional.of(announcement)
            every { userRepository.findById(userId) } returns Optional.of(user)
            every { scrapRepository.save(capture(scrapSlot)) } returns mockk()

            scrapService.scrap(userId, announcementId, request)

            verify { announcement.increaseScrapCount() }
            assertEquals(Color.BLUE.toHexString(), scrapSlot.captured.hexColor())
        }
    }

    @Nested
    @DisplayName("스크랩 색상 변경")
    inner class UpdateScrapTest {
        private val updateRequest = ScrapUpdateRequest(color = "RED")

        @Test
        @DisplayName("스크랩이 존재하지 않으면 예외가 발생한다")
        fun updateFailsIfScrapNotFound() {
            every {
                scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId)
            } returns null

            val exception =
                assertThrows(ScrapException::class.java) {
                    scrapService.updateScrap(userId, announcementId, updateRequest)
                }

            assertEquals(ScrapErrorCode.SCRAP_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("스크랩 색상을 성공적으로 업데이트한다")
        fun updateSucceeds() {
            val scrap = mockk<Scrap>(relaxed = true)

            every {
                scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId)
            } returns scrap
            every { scrap.updateColor(Color.RED) } returns Unit
            every { scrapRepository.save(scrap) } returns scrap

            scrapService.updateScrap(userId, announcementId, updateRequest)

            verify { scrap.updateColor(Color.RED) }
            verify { scrapRepository.save(scrap) }
        }
    }

    @Nested
    @DisplayName("스크랩 취소")
    inner class CancelScrapTest {
        @Test
        @DisplayName("스크랩이 존재하지 않으면 예외가 발생한다")
        fun cancelFailsIfScrapNotFound() {
            every {
                scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId)
            } returns null

            val exception =
                assertThrows(ScrapException::class.java) {
                    scrapService.cancelScrap(userId, announcementId)
                }

            assertEquals(ScrapErrorCode.SCRAP_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("공고가 존재하지 않으면 예외가 발생한다")
        fun cancelFailsIfAnnouncementNotFound() {
            val scrap = mockk<Scrap>()

            every {
                scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId)
            } returns scrap
            every {
                internshipAnnouncementRepository.findById(announcementId)
            } returns Optional.empty()

            val exception =
                assertThrows(ScrapException::class.java) {
                    scrapService.cancelScrap(userId, announcementId)
                }

            assertEquals(ScrapErrorCode.INTERN_SHIP_ANNOUNCEMENT_NOT_FOUND, exception.errorCode)
        }

        @Test
        @DisplayName("스크랩 취소에 성공한다")
        fun cancelSucceeds() {
            val scrap = mockk<Scrap>()
            val announcement = mockk<InternshipAnnouncement>(relaxed = true)

            every {
                scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, announcementId)
            } returns scrap
            every {
                internshipAnnouncementRepository.findById(announcementId)
            } returns Optional.of(announcement)
            every { scrapRepository.delete(scrap) } returns Unit

            scrapService.cancelScrap(userId, announcementId)

            verify { announcement.decreaseScrapCount() }
            verify { scrapRepository.delete(scrap) }
        }
    }
}
