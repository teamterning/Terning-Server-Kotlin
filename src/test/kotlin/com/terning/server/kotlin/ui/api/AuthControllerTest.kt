package com.terning.server.kotlin.ui.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.terning.server.kotlin.application.auth.AuthService
import com.terning.server.kotlin.application.auth.dto.SignInRequest
import com.terning.server.kotlin.application.auth.dto.SignInResponse
import com.terning.server.kotlin.application.auth.dto.SignUpRequest
import com.terning.server.kotlin.application.auth.dto.SignUpResponse
import com.terning.server.kotlin.config.TestSecurityConfig
import com.terning.server.kotlin.domain.auth.vo.AuthType
import com.terning.server.kotlin.domain.auth.vo.Token
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
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

    @Test
    fun `유저를 회원가입한다`() {
        // given
        val request =
            SignUpRequest(
                name = "이유빈",
                profileImage = "LUCKY",
                authType = "KAKAO",
                fcmToken = "fcmToken",
            )
        val response =
            SignUpResponse(
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                userId = 1L,
                authType = "KAKAO",
            )

        every { authService.signUpUser(any(), any()) } returns response

        // when & then
        mockMvc.post("/api/v1/auth/sign-up") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer 123456")
            content = objectMapper.writeValueAsString(request)
            with(csrf())
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.message") { value("회원가입에 성공하였습니다.") }
            jsonPath("$.result.accessToken") { value("accessToken") }
            jsonPath("$.result.refreshToken") { value("refreshToken") }
            jsonPath("$.result.userId") { value(1L) }
            jsonPath("$.result.authType") { value("KAKAO") }
        }
    }

    @Test
    fun `유저를 로그아웃한다`() {
        // given
        val authentication = UsernamePasswordAuthenticationToken(1L, null, emptyList())
        SecurityContextHolder.getContext().authentication = authentication

        every { authService.signOut(1L) } just Runs

        // when & then
        mockMvc.post("/api/v1/auth/logout")
            .andExpect {
                status { isOk() }
                jsonPath("$.message") { value("로그아웃에 성공하였습니다.") }
            }

        verify { authService.signOut(1L) }
    }
}
