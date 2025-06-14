package com.terning.server.kotlin.domain.scrap

import java.time.LocalDate

interface ScrapRepositoryCustom {
    fun existsByUserId(userId: Long): Boolean

    fun findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(
        userId: Long,
        start: LocalDate,
        end: LocalDate,
    ): List<Scrap>

    fun findScrapsByUserIdAndDeadlineOrderByDeadline(
        userId: Long,
        date: LocalDate,
    ): List<Scrap>
}
