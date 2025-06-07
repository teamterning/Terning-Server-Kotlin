package com.terning.server.kotlin.domain.scrap

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ScrapRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : ScrapRepositoryCustom {
    override fun findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(
        userId: Long,
        start: LocalDate,
        end: LocalDate,
    ): List<Scrap> {
        val scrap = QScrap.scrap

        return queryFactory
            .selectFrom(scrap)
            .where(
                scrap.user.id.eq(userId)
                    .and(scrap.internshipAnnouncement.internshipAnnouncementDeadline.value.between(start, end)),
            )
            .orderBy(scrap.internshipAnnouncement.internshipAnnouncementDeadline.value.asc())
            .fetch()
    }
}
