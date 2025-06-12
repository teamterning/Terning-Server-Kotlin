package com.terning.server.kotlin.domain.scrap

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ScrapRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : ScrapRepositoryCustom {
    override fun existsByUserId(userId: Long): Boolean {
        val scrap = QScrap.scrap
        val fetchFirst =
            queryFactory
                .selectOne()
                .from(scrap)
                .where(scrap.user.id.eq(userId))
                .fetchFirst()
        return fetchFirst != null
    }

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

    override fun findScrapsByUserIdAndDeadlineOrderByDeadline(
        userId: Long,
        date: LocalDate,
    ): List<Scrap> {
        val scrap = QScrap.scrap

        return queryFactory
            .selectFrom(scrap)
            .where(
                scrap.user.id.eq(userId)
                    .and(scrap.internshipAnnouncement.internshipAnnouncementDeadline.value.eq(date)),
            )
            .orderBy(scrap.internshipAnnouncement.internshipAnnouncementDeadline.value.asc())
            .fetch()
    }
}
