package com.terning.server.kotlin.domain.internshipAnnouncement

import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.terning.server.kotlin.domain.filter.Filter
import com.terning.server.kotlin.domain.internshipAnnouncement.QInternshipAnnouncement.internshipAnnouncement
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementMonth
import com.terning.server.kotlin.domain.internshipAnnouncement.vo.InternshipAnnouncementYear
import com.terning.server.kotlin.domain.scrap.QScrap.scrap
import com.terning.server.kotlin.domain.user.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class InternshipAnnouncementRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : InternshipAnnouncementRepositoryCustom {
    override fun findAllInternshipsWithScrapInfo(
        user: User,
        sortBy: String,
        pageable: Pageable,
    ): Page<Tuple> {
        val sort = SortType.from(sortBy)
        val query = baseQuery(user).orderBy(sort.orderSpecifier)

        val total = query.clone().select(internshipAnnouncement.count()).fetchOne() ?: 0L
        val content =
            query
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        return PageImpl(content, pageable, total)
    }

    override fun findFilteredInternshipsWithScrapInfo(
        user: User,
        filter: Filter,
        sortBy: String,
        pageable: Pageable,
    ): Page<Tuple> {
        val workingPeriod = filter.workingPeriod().toInternshipWorkingPeriod()
        val year = InternshipAnnouncementYear.from(filter.startDate().filterYear.value)
        val month = InternshipAnnouncementMonth.from(filter.startDate().filterMonth.value)

        val sort = SortType.from(sortBy)
        val query =
            baseQuery(user)
                .where(
                    internshipAnnouncement.workingPeriod.eq(workingPeriod),
                    internshipAnnouncement.startDate.year.eq(year),
                    internshipAnnouncement.startDate.month.eq(month),
                )
                .orderBy(sort.orderSpecifier)

        val total = query.clone().select(internshipAnnouncement.count()).fetchOne() ?: 0L
        val content =
            query
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        return PageImpl(content, pageable, total)
    }

    private fun baseQuery(user: User): JPAQuery<Tuple> {
        return queryFactory
            .select(internshipAnnouncement, scrap.id, scrap.color)
            .from(internshipAnnouncement)
            .leftJoin(scrap)
            .on(
                scrap.internshipAnnouncement.eq(internshipAnnouncement)
                    .and(scrap.user.eq(user)),
            )
    }
}
