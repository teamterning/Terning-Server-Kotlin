package com.terning.server.kotlin.domain.filter

import com.terning.server.kotlin.domain.user.User

interface FilterRepositoryCustom {
    fun findLatestByUser(user: User): Filter?
}
