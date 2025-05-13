package com.terning.server.kotlin.domain.user

import com.terning.server.kotlin.utils.BaseRootEntity
import jakarta.persistence.*

@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(length = 12)
    var name: String,
    @Enumerated(EnumType.STRING)
    @Column(length = 12)
    var profileImage: ProfileImage,
    @Enumerated(EnumType.STRING)
    @Column(length = 12)
    var userState: UserState,
) : BaseRootEntity() {
    fun getName(): String = name

    fun updateProfile(
        name: String,
        profileImage: ProfileImage,
    ) {
        this.name = name
        this.profileImage = profileImage
    }
}
