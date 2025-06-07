package com.terning.server.kotlin.application.filter

import com.terning.server.kotlin.domain.filter.FilterRepository
import com.terning.server.kotlin.domain.user.exception.UserErrorCode
import com.terning.server.kotlin.domain.user.exception.UserException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FilterService(
    private val filterRepository: FilterRepository,
) {
    @Transactional
    fun getUserFilter(userId: Long): FilterResponse {
        val filter =
            filterRepository.findById(userId).orElseThrow {
                UserException(UserErrorCode.NOT_FOUND_USER_EXCEPTION)
            }

        return FilterResponse(
            jobType = filter.jobType().type,
            grade = filter.grade().type,
            workingPeriod = filter.workingPeriod().period,
            startYear = filter.startDate().filterYear.value,
            startMonth = filter.startDate().filterMonth.value,
        )
    }
}
