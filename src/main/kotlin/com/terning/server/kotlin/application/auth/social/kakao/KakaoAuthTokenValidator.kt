package com.terning.server.kotlin.application.auth.social.kakao

import com.fasterxml.jackson.databind.ObjectMapper
import com.terning.server.kotlin.domain.auth.exception.AuthErrorCode
import com.terning.server.kotlin.domain.auth.exception.AuthException
import com.terning.server.kotlin.domain.common.config.ValueConfig
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate

@Component
@Transactional
class KakaoAuthTokenValidator(
    private val valueConfig: ValueConfig,
    private val restTemplate: RestTemplate,
    private val objectMapper: ObjectMapper,
) {
    fun extractKakaoId(authAccessToken: String): String =
        try {
            val headers =
                HttpHeaders().apply {
                    add("Authorization", authAccessToken)
                }
            val entity = HttpEntity<Any>(headers)
            val response = restTemplate.postForEntity(valueConfig.kakaoUri, entity, Any::class.java)
            val body = objectMapper.convertValue(response.body, Map::class.java)
            body["id"].toString()
        } catch (e: Exception) {
            throw AuthException(AuthErrorCode.FAILED_SOCIAL_LOGIN)
        }
}
