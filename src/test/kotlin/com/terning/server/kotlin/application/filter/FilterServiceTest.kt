package com.terning.server.kotlin.application.filter

import com.terning.server.kotlin.domain.filter.Filter
import com.terning.server.kotlin.domain.filter.FilterRepository
import com.terning.server.kotlin.domain.filter.exception.FilterErrorCode
import com.terning.server.kotlin.domain.filter.exception.FilterException
import com.terning.server.kotlin.domain.filter.vo.FilterGrade
import com.terning.server.kotlin.domain.filter.vo.FilterJobType
import com.terning.server.kotlin.domain.filter.vo.FilterMonth
import com.terning.server.kotlin.domain.filter.vo.FilterStartDate
import com.terning.server.kotlin.domain.filter.vo.FilterWorkingPeriod
import com.terning.server.kotlin.domain.filter.vo.FilterYear
import com.terning.server.kotlin.domain.user.User
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.Optional

class FilterServiceTest {
    private val filterRepository: FilterRepository = mockk()

    private lateinit var filterService: FilterService

    @BeforeEach
    fun setUp() {
        filterService = FilterService(filterRepository = filterRepository)
    }

    @Test
    @DisplayName("사용자를 찾을 수 없으면 에러가 발생한다")
    fun getFilterFailsIfUserNotFound() {
        // given
        val userId = 1L
        every { filterRepository.findById(userId) } returns Optional.empty()

        // when & then
        val exception =
            assertThrows(FilterException::class.java) {
                filterService.getUserFilter(userId)
            }

        assertThat(exception.errorCode).isEqualTo(FilterErrorCode.NOT_FOUND_USER_EXCEPTION)
    }

    @Test
    @DisplayName("필터 정보를 성공적으로 조회한다")
    fun getFilterInformation() {
        // given
        val userId = 1L
        val user = mockk<User>()
        val filter =
            Filter.of(
                user = user,
                filterJobType = FilterJobType.IT,
                filterGrade = FilterGrade.SENIOR,
                filterWorkingPeriod = FilterWorkingPeriod.SHORT_TERM,
                filterStartDate =
                    FilterStartDate.of(
                        filterYear = FilterYear.from(2025),
                        filterMonth = FilterMonth.from(6),
                    ),
            )

        every { filterRepository.findById(userId) } returns Optional.of(filter)

        // when
        val result = filterService.getUserFilter(userId)

        // then
        assertThat(result.jobType).isEqualTo("it")
        assertThat(result.grade).isEqualTo("senior")
        assertThat(result.workingPeriod).isEqualTo("short")
        assertThat(result.startYear).isEqualTo(2025)
        assertThat(result.startMonth).isEqualTo(6)
    }
}