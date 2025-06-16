package com.terning.server.kotlin.ui.api

import com.ninjasquad.springmockk.MockkBean
import com.terning.server.kotlin.application.search.SearchService
import com.terning.server.kotlin.application.search.dto.SearchAnnouncementResponse
import com.terning.server.kotlin.application.search.dto.SearchPageResponse
import com.terning.server.kotlin.application.search.dto.ViewCountAnnouncementResponse
import com.terning.server.kotlin.application.search.dto.ViewCountResponse
import io.mockk.every
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(SearchController::class)
@ActiveProfiles("test")
class SearchControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var searchService: SearchService

    @Test
    @DisplayName("키워드로 공고를 검색하고 성공적으로 결과를 반환한다")
    fun searchSuccessfully() {
        // given
        val userId = 1L
        val keyword = "네이버"
        val sortBy = "DEADLINE_SOON"
        val pageable = PageRequest.of(0, 10)

        val searchResponse =
            SearchPageResponse(
                totalPages = 1,
                totalCount = 1,
                hasNext = false,
                announcements =
                    listOf(
                        SearchAnnouncementResponse(
                            internshipAnnouncementId = 10L,
                            companyImage = "https://naver.com/logo.png",
                            dDay = "D-10",
                            title = "[네이버] 백엔드 개발 인턴 모집",
                            workingPeriod = "3개월",
                            isScrapped = true,
                            color = "#FFFFFF",
                            deadline = "2025년 6월 26일",
                            startYearMonth = "2025년 7월",
                        ),
                    ),
            )

        every { searchService.search(userId, keyword, sortBy, pageable) } returns searchResponse

        // when & then
        mockMvc.get("/api/v1/search") {
            param("keyword", keyword)
            param("sortBy", sortBy)
            param("page", pageable.pageNumber.toString())
            param("size", pageable.pageSize.toString())
        }.andExpect {
            status { isOk() }
            jsonPath("$.status") { value(200) }
            jsonPath("$.message") { value("검색에 성공했습니다.") }
            jsonPath("$.result.totalCount") { value(1) }
            jsonPath("$.result.announcements[0].internshipAnnouncementId") { value(10) }
            jsonPath("$.result.announcements[0].title") { value("[네이버] 백엔드 개발 인턴 모집") }
            jsonPath("$.result.announcements[0].isScrapped") { value(true) }
        }
    }

    @Test
    @DisplayName("파라미터 없이 요청 시 기본값으로 검색하고 결과를 반환한다")
    fun searchWithDefaultParameters() {
        // given
        val userId = 1L
        val pageable = PageRequest.of(0, 10)

        val searchResponse =
            SearchPageResponse(
                totalPages = 1,
                totalCount = 1,
                hasNext = false,
                announcements =
                    listOf(
                        SearchAnnouncementResponse(
                            internshipAnnouncementId = 11L,
                            companyImage = "https://kakao.com/logo.png",
                            dDay = "D-5",
                            title = "[카카오] 프론트엔드 개발 인턴 모집",
                            workingPeriod = "6개월",
                            isScrapped = false,
                            color = null,
                            deadline = "2025년 6월 21일",
                            startYearMonth = "2025년 8월",
                        ),
                    ),
            )

        every { searchService.search(userId, null, "DEADLINE_SOON", pageable) } returns searchResponse

        // when & then
        mockMvc.get("/api/v1/search").andExpect {
            status { isOk() }
            jsonPath("$.status") { value(200) }
            jsonPath("$.message") { value("검색에 성공했습니다.") }
            jsonPath("$.result.totalCount") { value(1) }
            jsonPath("$.result.announcements[0].internshipAnnouncementId") { value(11) }
            jsonPath("$.result.announcements[0].title") { value("[카카오] 프론트엔드 개발 인턴 모집") }
            jsonPath("$.result.announcements[0].isScrapped") { value(false) }
        }
    }

    @Test
    @DisplayName("조회수 많은 공고를 성공적으로 조회한다")
    fun getMostViewedAnnouncementsSuccessfully() {
        // given
        val userId = 1L
        val viewCountResponse =
            ViewCountResponse(
                announcements =
                    listOf(
                        ViewCountAnnouncementResponse(
                            internshipAnnouncementId = 23L,
                            companyImage = "image_url_1",
                            title = "인기 공고 1",
                        ),
                        ViewCountAnnouncementResponse(
                            internshipAnnouncementId = 3L,
                            companyImage = "image_url_2",
                            title = "인기 공고 2",
                        ),
                    ),
            )

        every { searchService.getMostViewedAnnouncements(userId) } returns viewCountResponse

        // when & then
        mockMvc.get("/api/v1/search/views")
            .andExpect {
                status { isOk() }
                jsonPath("$.status") { value(200) }
                jsonPath("$.message") { value("탐색 > 조회수 많은 공고를 조회하는데 성공했습니다") }
                jsonPath("$.result.announcements.size()") { value(2) }
                jsonPath("$.result.announcements[0].internshipAnnouncementId") { value(23L) }
                jsonPath("$.result.announcements[0].title") { value("인기 공고 1") }
            }
    }

    @Test
    @DisplayName("스크랩 많은 공고를 성공적으로 조회한다")
    fun getMostScrappedAnnouncementsSuccessfully() {
        // given
        val userId = 1L
        val scrapCountResponse =
            ViewCountResponse(
                announcements =
                    listOf(
                        ViewCountAnnouncementResponse(
                            internshipAnnouncementId = 50L,
                            companyImage = "image_scrap_1",
                            title = "스크랩 많은 공고 1",
                        ),
                        ViewCountAnnouncementResponse(
                            internshipAnnouncementId = 51L,
                            companyImage = "image_scrap_2",
                            title = "스크랩 많은 공고 2",
                        ),
                    ),
            )

        every { searchService.getMostScrappedAnnouncements(userId) } returns scrapCountResponse

        // when & then
        mockMvc.get("/api/v1/search/scraps")
            .andExpect {
                status { isOk() }
                jsonPath("$.status") { value(200) }
                jsonPath("$.message") { value("탐색 > 스크랩 수 많은 공고를 조회하는데 성공했습니다") }
                jsonPath("$.result.announcements.size()") { value(2) }
                jsonPath("$.result.announcements[0].internshipAnnouncementId") { value(50L) }
                jsonPath("$.result.announcements[0].title") { value("스크랩 많은 공고 1") }
            }
    }
}
