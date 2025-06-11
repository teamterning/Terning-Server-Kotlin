package com.terning.server.kotlin.application.filter

import com.terning.server.kotlin.domain.filter.FilterRepository
import com.terning.server.kotlin.domain.filter.exception.FilterErrorCode
import com.terning.server.kotlin.domain.filter.exception.FilterException
import com.terning.server.kotlin.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FilterService(
    private val filterRepository: FilterRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun getUserFilter(userId: Long): FilterResponse {
        val user = userRepository.findById(userId).orElseThrow {
            FilterException(FilterErrorCode.NOT_FOUND_USER_EXCEPTION)
        }

        val filter = filterRepository.findLatestByUser(user)
            ?: throw FilterException(FilterErrorCode.NOT_FOUND_FILTER_EXCEPTION)

        val startDate = filter.startDate()

        return FilterResponse(
            jobType = filter.jobType().type,
            grade = filter.grade().type,
            workingPeriod = filter.workingPeriod().period,
            startYear = startDate.filterYear.value,
            startMonth = startDate.filterMonth.value,
        )
    }
}
