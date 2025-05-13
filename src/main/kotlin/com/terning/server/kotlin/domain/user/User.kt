package com.terning.server.kotlin.domain.user

import com.terning.server.kotlin.utils.BaseRootEntity
import jakarta.persistence.*

@Entity
class User(
    @Embedded
    var userInformation: UserInformation,
    @Enumerated(EnumType.STRING)
    @Column(length = 12)
    var userState: UserState,
    id: Long = 0L,
) : BaseRootEntity<User>(id) {
    fun getName(): String = userInformation.name

    fun updateProfile(
        name: String,
        profileImage: ProfileImage,
    ) {
        this.userInformation = UserInformation(name = name, profileImage = profileImage)
    }
}
