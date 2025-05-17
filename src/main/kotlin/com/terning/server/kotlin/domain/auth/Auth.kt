package com.terning.server.kotlin.domain.auth

import com.terning.server.kotlin.domain.common.BaseRootEntity
import com.terning.server.kotlin.domain.user.User
import jakarta.persistence.*

@Entity
@Table(name = "auth")
class Auth private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    val user: User,
    @Column(length = 255)
    val authId: String,
    @Column(length = 12)
    val authType: AuthType,
    @Column(length = 255)
    val refreshToken: String,
) : BaseRootEntity()