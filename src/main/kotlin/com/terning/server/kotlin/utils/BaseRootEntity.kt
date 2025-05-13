package com.terning.server.kotlin.utils

import jakarta.persistence.*
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseJpaEntity {
    @CreatedDate
    @Column(updatable = false, nullable = false)
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    @Column(nullable = false)
    lateinit var updatedAt: LocalDateTime
}

@MappedSuperclass
abstract class BaseRootEntity<T>(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : BaseJpaEntity()
