package com.terning.server.kotlin.application.scrap

import com.terning.server.kotlin.application.scrap.dto.ScrapRequest
import com.terning.server.kotlin.application.scrap.dto.ScrapUpdateRequest
import com.terning.server.kotlin.domain.internshipAnnouncement.InternshipAnnouncementRepository
import com.terning.server.kotlin.domain.scrap.Scrap
import com.terning.server.kotlin.domain.scrap.ScrapRepository
import com.terning.server.kotlin.domain.scrap.exception.ScrapErrorCode
import com.terning.server.kotlin.domain.scrap.exception.ScrapException
import com.terning.server.kotlin.domain.scrap.vo.Color
import com.terning.server.kotlin.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ScrapService(
    private val scrapRepository: ScrapRepository,
    private val userRepository: UserRepository,
    private val internshipAnnouncementRepository: InternshipAnnouncementRepository,
) {
    @Transactional
    fun scrap(
        userId: Long,
        internshipAnnouncementId: Long,
        scrapRequest: ScrapRequest,
    ) {
        if (scrapRepository.existsByUserIdAndInternshipAnnouncementId(userId, internshipAnnouncementId)) {
            throw ScrapException(ScrapErrorCode.EXISTS_SCRAP_ALREADY)
        }

        val announcement =
            internshipAnnouncementRepository.findById(internshipAnnouncementId)
                .orElseThrow { ScrapException(ScrapErrorCode.INTERN_SHIP_ANNOUNCEMENT_NOT_FOUND) }

        val user =
            userRepository.findById(userId)
                .orElseThrow { ScrapException(ScrapErrorCode.USER_NOT_FOUND) }

        val scrap =
            Scrap.of(
                user = user,
                internshipAnnouncement = announcement,
                color = Color.from(scrapRequest.color),
            )

        scrapRepository.save(scrap)
        announcement.increaseScrapCount()
    }

    @Transactional
    fun updateScrap(
        userId: Long,
        internshipAnnouncementId: Long,
        scrapUpdateRequest: ScrapUpdateRequest,
    ) {
        val scrap =
            scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, internshipAnnouncementId)
                ?: throw ScrapException(ScrapErrorCode.SCRAP_NOT_FOUND)

        scrap.updateColor(Color.from(scrapUpdateRequest.color))
        scrapRepository.save(scrap)
    }

    @Transactional
    fun cancelScrap(
        userId: Long,
        internshipAnnouncementId: Long,
    ) {
        val scrap =
            scrapRepository.findByUserIdAndInternshipAnnouncementId(userId, internshipAnnouncementId)
                ?: throw ScrapException(ScrapErrorCode.SCRAP_NOT_FOUND)

        val announcement =
            internshipAnnouncementRepository.findById(internshipAnnouncementId)
                .orElseThrow { ScrapException(ScrapErrorCode.INTERN_SHIP_ANNOUNCEMENT_NOT_FOUND) }

        announcement.decreaseScrapCount()
        scrapRepository.delete(scrap)
    }
}
