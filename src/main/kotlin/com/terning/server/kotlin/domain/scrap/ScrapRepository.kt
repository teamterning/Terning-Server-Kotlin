package com.terning.server.kotlin.domain.scrap

import org.springframework.data.jpa.repository.JpaRepository

interface ScrapRepository : JpaRepository<Scrap, Long> {
    fun existsByUserIdAndInternshipAnnouncementId(
        userId: Long,
        internshipAnnouncementId: Long,
    ): Boolean

    fun findByUserIdAndInternshipAnnouncementId(
        userId: Long,
        internshipAnnouncementId: Long,
    ): Scrap?
}
