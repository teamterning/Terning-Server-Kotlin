package com.terning.server.kotlin.domain.scrap

import com.terning.server.kotlin.domain.common.BaseRootEntity
import com.terning.server.kotlin.domain.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.Hibernate

@Entity
@Table(name = "scraps")
class Scrap private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private var color: Color,
) : BaseRootEntity() {
    companion object {
        fun of(
            user: User,
            color: Color,
        ): Scrap = Scrap(user = user, color = color)
    }

    fun changeColor(to: Color) {
        this.color = to
    }

    fun hexColor(): String = color.toHexString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as Scrap
        return id != null && id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}
