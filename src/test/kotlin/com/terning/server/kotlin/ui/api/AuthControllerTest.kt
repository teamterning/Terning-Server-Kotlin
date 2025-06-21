package com.terning.server.kotlin.ui.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.terning.server.kotlin.application.auth.AuthService
import com.terning.server.kotlin.application.auth.dto.SignInRequest
import com.terning.server.kotlin.application.auth.dto.SignInResponse
import com.terning.server.kotlin.config.TestSecurityConfig
import com.terning.server.kotlin.domain.auth.vo.AuthType
import com.terning.server.kotlin.domain.auth.vo.Token
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(AuthController::class)
@Import(TestSecurityConfig::class)
@ActiveProfiles("test")
class AuthControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var authService: AuthService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `유저를 로그인한다`() {
        // given
        val accessTokenHeader = "Bearer dummyKakaoToken"
        val request =
            SignInRequest(
                authType = "KAKAO",
                fcmToken = "fcmToken",
            )
        val response =
            SignInResponse.of(
                token = Token("accessToken", "refreshToken"),
                userId = 1L,
                authId = "123456",
                authType = AuthType.KAKAO,
            )

        every {
            authService.signInUser(
                eq("Bearer dummyKakaoToken"),
                any(),
            )
        } returns response

        // when & then
        mockMvc.post("/api/v1/auth/sign-in") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header("Authorization", accessTokenHeader)
            content = objectMapper.writeValueAsString(request)
            with(csrf())
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.message") { value("로그인에 성공했습니다.") }
            jsonPath("$.result.userId") { value(1) }
            jsonPath("$.result.accessToken") { value("accessToken") }
            jsonPath("$.result.refreshToken") { value("refreshToken") }
        }
    }
}
