package com.terning.server.kotlin.domain.internshipAnnouncement

import com.querydsl.core.Tuple
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.CaseBuilder
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
import org.springframework.util.StringUtils
import java.time.LocalDate

@Repository
class InternshipAnnouncementRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : InternshipAnnouncementRepositoryCustom {
    override fun findAllInternshipsWithScrapInfo(
        user: User,
        sortBy: String,
        pageable: Pageable,
        now: LocalDate,
    ): Page<Tuple> {
        val content =
            baseQuery(user)
                .orderBy(*createOrderSpecifier(sortBy, now))
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        val total = countQuery(null).fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }

    override fun findFilteredInternshipsWithScrapInfo(
        user: User,
        filter: Filter,
        sortBy: String,
        pageable: Pageable,
        now: LocalDate,
    ): Page<Tuple> {
        val workingPeriod = filter.workingPeriod().toInternshipWorkingPeriod()
        val year = InternshipAnnouncementYear.from(filter.startDate().filterYear.value)
        val month = InternshipAnnouncementMonth.from(filter.startDate().filterMonth.value)

        val whereClause =
            internshipAnnouncement.workingPeriod.eq(workingPeriod)
                .and(internshipAnnouncement.startDate.year.eq(year))
                .and(internshipAnnouncement.startDate.month.eq(month))

        val content =
            baseQuery(user)
                .where(whereClause)
                .orderBy(*createOrderSpecifier(sortBy, now))
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        val total =
            queryFactory
                .select(internshipAnnouncement.count())
                .from(internshipAnnouncement)
                .where(whereClause)
                .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }

    override fun findByKeywordWithScrapInfo(
        user: User,
        keyword: String?,
        sortBy: String,
        pageable: Pageable,
        now: LocalDate,
    ): Page<Tuple> {
        val content =
            baseQuery(user)
                .where(keywordContains(keyword))
                .orderBy(*createOrderSpecifier(sortBy, now))
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        val total = countQuery(keyword).fetchOne() ?: 0L

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

    private fun keywordContains(keyword: String?): BooleanExpression? {
        if (!StringUtils.hasText(keyword)) {
            return null
        }
        return internshipAnnouncement.title.value.containsIgnoreCase(keyword)
            .or(internshipAnnouncement.company.name.value.containsIgnoreCase(keyword))
    }

    private fun createOrderSpecifier(
        sortBy: String,
        now: LocalDate,
    ): Array<OrderSpecifier<*>> {
        val sort = SortType.from(sortBy)

        if (sort == SortType.DEADLINE_SOON) {
            val deadlineOrder =
                CaseBuilder()
                    .`when`(internshipAnnouncement.internshipAnnouncementDeadline.value.lt(now))
                    .then(1)
                    .otherwise(0)
                    .asc()
            return arrayOf(deadlineOrder, sort.orderSpecifier)
        }

        return arrayOf(sort.orderSpecifier)
    }

    private fun countQuery(keyword: String?): JPAQuery<Long> {
        return queryFactory
            .select(internshipAnnouncement.count())
            .from(internshipAnnouncement)
            .where(keywordContains(keyword))
    }
}
