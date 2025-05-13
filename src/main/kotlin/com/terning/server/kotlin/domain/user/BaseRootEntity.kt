package com.terning.server.kotlin.domain.user

import jakarta.persistence.*
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseRootEntity<T>(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
