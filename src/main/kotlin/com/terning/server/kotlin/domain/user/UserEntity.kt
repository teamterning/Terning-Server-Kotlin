package com.terning.server.kotlin.domain.user

import com.terning.server.kotlin.utils.BaseRootEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "Users")
@Entity
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Embedded
    var name: UserName,
    @Embedded
    var profileImage: UserProfileImage,
    @Enumerated(EnumType.STRING)
    @Column(length = 12)
    var userState: UserState,
) : BaseRootEntity() {
    fun getName(): String = name.value

    fun updateProfile(
        name: UserName,
        profileImage: UserProfileImage,
    ) {
        this.name = name
        this.profileImage = profileImage
    }
}
