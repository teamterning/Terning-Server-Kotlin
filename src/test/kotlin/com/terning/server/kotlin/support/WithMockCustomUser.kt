package com.terning.server.kotlin.support

import org.springframework.security.test.context.support.WithSecurityContext
import kotlin.annotation.AnnotationRetention.RUNTIME

@Retention(RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class WithMockCustomUser(
    val userId: Long = 1L,
)
