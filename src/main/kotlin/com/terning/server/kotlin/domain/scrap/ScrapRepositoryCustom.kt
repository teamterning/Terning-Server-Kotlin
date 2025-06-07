package com.terning.server.kotlin.domain.scrap

import java.time.LocalDate

interface ScrapRepositoryCustom {
    fun findScrapsByUserIdAndDeadlineBetweenOrderByDeadline(
        userId: Long,
        start: LocalDate,
        end: LocalDate,
    ): List<Scrap>
}
