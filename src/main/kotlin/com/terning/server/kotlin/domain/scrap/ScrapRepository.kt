package com.terning.server.kotlin.domain.scrap

import org.springframework.data.jpa.repository.JpaRepository

interface ScrapRepository : JpaRepository<Scrap, Long> {
    fun existsByInternshipAnnouncementIdAndUserId(
        userId: Long,
        internshipAnnouncementId: Long,
    ): Boolean
}
