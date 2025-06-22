package com.terning.server.kotlin.application.auth.config

import com.terning.server.kotlin.application.auth.social.SocialAuthProvider
import com.terning.server.kotlin.application.auth.social.SocialAuthServiceManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SocialAuthConfig(
    private val providers: List<SocialAuthProvider>,
) {
    @Bean
    fun socialAuthServiceManager(): SocialAuthServiceManager = SocialAuthServiceManager.create(providers)
}
