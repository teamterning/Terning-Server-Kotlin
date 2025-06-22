package com.terning.server.kotlin.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

/**
 * Controller 테스트 시 Spring Security 필터 체인을 비활성화하여
 * 인증 로직을 우회하기 위한 설정입니다.
 */
@TestConfiguration
class TestSecurityConfig {
    @Bean
    fun testSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .httpBasic { it.disable() }
        return http.build()
    }
}
