package com.terning.server.kotlin.domain.auth

import com.terning.server.kotlin.domain.common.BaseRootEntity
import com.terning.server.kotlin.domain.user.User
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "auth")
class Auth private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    val user: User,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "authId"))
    private var authId: AuthId,

    @Enumerated(EnumType.STRING)
    @Column(length = 12)
    private var authType: AuthType,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "refreshToken"))
    private var refreshToken: RefreshToken,
) : BaseRootEntity() {
    fun updateRefreshToken(newRefreshToken: RefreshToken) {
        this.refreshToken = newRefreshToken
    }

    fun resetRefreshToken() {
        try {
            this.refreshToken = RefreshToken(null)
        } catch (e: Exception) {
            throw AuthException(AuthErrorCode.FAILED_REFRESH_TOKEN_RESET)
        }
    }
}
