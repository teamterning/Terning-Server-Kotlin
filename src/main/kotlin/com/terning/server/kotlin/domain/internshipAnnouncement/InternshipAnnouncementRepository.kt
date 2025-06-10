package com.terning.server.kotlin.domain.internshipAnnouncement

import org.springframework.data.jpa.repository.JpaRepository

interface InternshipAnnouncementRepository : JpaRepository<InternshipAnnouncement, Long>, InternshipAnnouncementRepositoryCustom
