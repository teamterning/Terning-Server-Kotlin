package com.terning.server.kotlin.application

import com.terning.server.kotlin.application.scrap.ScrapRequest
import com.terning.server.kotlin.application.scrap.ScrapUpdateRequest
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
@Transactional(readOnly = false)
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
        if (scrapRepository.existsByInternshipAnnouncementIdAndUserId(userId, internshipAnnouncementId)) {
            throw ScrapException(ScrapErrorCode.EXISTS_SCRAP_ALREADY)
        }

        val announcement =
            internshipAnnouncementRepository.findById(internshipAnnouncementId)
                .orElseThrow { ScrapException(ScrapErrorCode.INTERN_SHIP_ANNOUNCEMENT_NOT_FOUND) }

        val user =
            userRepository.findById(userId)
                .orElseThrow { ScrapException(ScrapErrorCode.USER_NOT_FOUND) }

        val color = Color.from(scrapRequest.color)
        val scrap = Scrap.of(user, announcement, color)

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
            scrapRepository.findByInternshipAnnouncementIdAndUserId(userId, internshipAnnouncementId)
                ?: throw ScrapException(ScrapErrorCode.SCRAP_NOT_FOUND)

        val color = Color.from(scrapUpdateRequest.color)
        scrap.updateColor(color)

        scrapRepository.save(scrap)
    }
}
