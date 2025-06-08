package com.terning.server.kotlin.domain.user

import com.terning.server.kotlin.domain.common.BaseRootEntity
import com.terning.server.kotlin.domain.user.vo.ProfileImage
import com.terning.server.kotlin.domain.user.vo.UserName
import com.terning.server.kotlin.domain.user.vo.UserState
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Embedded
    @AttributeOverride(
        name = "value",
        column = Column(name = "name", length = 12, nullable = false),
    )
    private var name: UserName,

    @Enumerated(EnumType.STRING)
    @Column(name = "profileImage", length = 12, nullable = false)
    private var profileImage: ProfileImage,

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 12, nullable = false)
    private var userState: UserState,
) : BaseRootEntity() {
    fun name(): String = name.value

    fun profileImage(): ProfileImage = profileImage

    fun userState(): UserState = userState

    fun updateProfile(
        newName: UserName,
        newProfileImage: ProfileImage,
    ) {
        this.name = newName
        this.profileImage = newProfileImage
    }

    fun deactivate() {
        this.userState = UserState.INACTIVE
    }

    fun ban() {
        this.userState = UserState.BANNED
    }

    fun activate() {
        this.userState = UserState.ACTIVE
    }

    fun isActive(): Boolean = userState == UserState.ACTIVE

    companion object {
        fun of(
            name: String,
            profile: String,
            userState: UserState = UserState.ACTIVE,
        ): User =
            User(
                name = UserName.from(name),
                profileImage = ProfileImage.from(profile),
                userState = userState,
            )
    }
}
