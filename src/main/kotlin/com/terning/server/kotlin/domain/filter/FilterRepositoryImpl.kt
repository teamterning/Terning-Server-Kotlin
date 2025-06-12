package com.terning.server.kotlin.infrastructure.filter

import com.querydsl.jpa.impl.JPAQueryFactory
import com.terning.server.kotlin.domain.filter.Filter
import com.terning.server.kotlin.domain.filter.FilterRepositoryCustom
import com.terning.server.kotlin.domain.filter.QFilter.filter
import com.terning.server.kotlin.domain.user.User
import org.springframework.stereotype.Repository

@Repository
class FilterRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : FilterRepositoryCustom {
    override fun findLatestByUser(user: User): Filter? {
        return queryFactory
            .selectFrom(filter)
            .where(filter.user.eq(user))
            .orderBy(filter.createdAt.desc())
            .limit(1)
            .fetchOne()
    }
}
