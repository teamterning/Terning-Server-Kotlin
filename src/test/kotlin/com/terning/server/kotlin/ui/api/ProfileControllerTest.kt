package com.terning.server.kotlin.ui.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.terning.server.kotlin.application.profile.ProfileRequest
import com.terning.server.kotlin.application.profile.ProfileResponse
import com.terning.server.kotlin.application.profile.ProfileService
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch

@WebMvcTest(ProfileController::class)
@ActiveProfiles("test")
class ProfileControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var profileService: ProfileService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var profileResponse: ProfileResponse
    private lateinit var profileRequest: ProfileRequest

    @BeforeEach
    fun setUp() {
        profileResponse =
            ProfileResponse(
                name = "이유빈",
                profileImage = "BASIC",
                authType = "KAKAO",
            )
        profileRequest =
            ProfileRequest(
                name = "이유빈 수정",
                profileImage = "LUCKY",
            )
    }

    @Test
    @DisplayName("유저 정보를 가져온다")
    fun getProfile() {
        // given
        val userId = 1L
        every { profileService.getUserProfile(userId = userId) } returns profileResponse

        // when
        mockMvc.get("/api/v1/mypage/profile") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            // then
            status { isCreated() }
            jsonPath("$.result.name") { value("이유빈") }
            jsonPath("$.result.profileImage") { value("BASIC") }
            jsonPath("$.result.authType") { value("KAKAO") }
        }
    }

    @Test
    @DisplayName("유저 정보를 수정한다")
    fun updateProfile() {
        // given
        val userId = 1L
        every {
            profileService.updateUserProfile(
                userId = userId,
                profileRequest = profileRequest,
            )
        } just runs

        // when
        mockMvc.patch("/api/v1/mypage/profile") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(profileRequest)
        }.andExpect {
            // then
            status { isOk() }
        }
    }
}
