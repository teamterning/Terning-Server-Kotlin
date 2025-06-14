package com.terning.server.kotlin.application.filter

import com.terning.server.kotlin.application.filter.dto.UpdateFilterRequest
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
import com.terning.server.kotlin.domain.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.Optional

class FilterServiceTest {
    private val filterRepository: FilterRepository = mockk()
    private val userRepository: UserRepository = mockk()

    private lateinit var filterService: FilterService

    private val userId = 1L

    @BeforeEach
    fun setUp() {
        filterService = FilterService(filterRepository, userRepository)
    }

    @Test
    @DisplayName("사용자를 찾을 수 없으면 에러가 발생한다")
    fun getFilterFailsIfUserNotFound() {
        // given
        every { userRepository.findById(userId) } returns Optional.empty()

        // when & then
        val exception =
            assertThrows(FilterException::class.java) {
                filterService.getUserFilter(userId)
            }

        assertThat(exception.errorCode).isEqualTo(FilterErrorCode.NOT_FOUND_USER_EXCEPTION)
    }

    @Test
    @DisplayName("사용자는 있지만 필터가 없으면 에러가 발생한다")
    fun getFilterFailsIfFilterNotFound() {
        // given
        val user = mockk<User>()

        every { userRepository.findById(userId) } returns Optional.of(user)
        every { filterRepository.findLatestByUser(user) } returns null

        // when & then
        val exception =
            assertThrows(FilterException::class.java) {
                filterService.getUserFilter(userId)
            }

        assertThat(exception.errorCode).isEqualTo(FilterErrorCode.NOT_FOUND_FILTER_EXCEPTION)
    }

    @Test
    @DisplayName("필터 정보를 성공적으로 조회한다")
    fun getFilterInformation() {
        // given
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

        every { userRepository.findById(userId) } returns Optional.of(user)
        every { filterRepository.findLatestByUser(user) } returns filter

        // when
        val result = filterService.getUserFilter(userId)

        // then
        assertThat(result.jobType).isEqualTo("it")
        assertThat(result.grade).isEqualTo("senior")
        assertThat(result.workingPeriod).isEqualTo("short")
        assertThat(result.startYear).isEqualTo(2025)
        assertThat(result.startMonth).isEqualTo(6)
    }

    @Test
    @DisplayName("필터링 정보를 성공적으로 업데이트 한다")
    fun updateFilterSucceeds() {
        // given
        val user = mockk<User>()
        val filter = mockk<Filter>(relaxed = true)
        val updateFilterRequest =
            UpdateFilterRequest(
                jobType = "plan",
                grade = "sophomore",
                workingPeriod = "middle",
                startYear = 2025,
                startMonth = 2,
            )

        every { userRepository.findById(userId) } returns Optional.of(user)
        every { filterRepository.findLatestByUser(user) } returns filter

        // when
        filterService.updateUserFilter(
            userId = userId,
            updateFilterRequest = updateFilterRequest,
        )

        // then
        verify {
            filter.updateFilter(
                newFilterJobType = FilterJobType.from("plan"),
                newFilterGrade = FilterGrade.from("sophomore"),
                newFilterWorkingPeriod = FilterWorkingPeriod.from("middle"),
                newFilterStartDate =
                    FilterStartDate.of(
                        filterYear = FilterYear.from(2025),
                        filterMonth = FilterMonth.from(2),
                    ),
            )
        }
    }
}
