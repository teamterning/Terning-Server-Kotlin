package com.terning.server.kotlin.domain.internshipAnnouncement

import com.querydsl.core.Tuple
import com.terning.server.kotlin.domain.filter.Filter
import com.terning.server.kotlin.domain.user.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface InternshipAnnouncementRepositoryCustom {
    fun findAllInternshipsWithScrapInfo(
        user: User,
        sortBy: String,
        pageable: Pageable,
    ): Page<Tuple>

    fun findFilteredInternshipsWithScrapInfo(
        user: User,
        filter: Filter,
        sortBy: String,
        pageable: Pageable,
    ): Page<Tuple>
}
