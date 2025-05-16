package com.terning.server.kotlin.domain.user

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UserTest {
    @Nested
    @DisplayName("updateProfile(name, image)는")
    inner class UpdateProfile {
        @Test
        @DisplayName("이름과 프로필 이미지를 변경한다")
        fun updatesNameAndProfileImage() {
            val user = createUser()
            val newName = UserName("새이름")
            val newImage = ProfileImage.LUCKY

            user.updateProfile(newName, newImage)

            assertThat(user.name()).isEqualTo("새이름")
            assertThat(user.profileImage()).isEqualTo(ProfileImage.LUCKY)
        }
    }

    @Nested
    @DisplayName("상태 변경 메서드는")
    inner class StateChanges {
        @Test
        @DisplayName("deactivate() 호출 시 상태가 INACTIVE가 된다")
        fun becomesInactive() {
            val user = createUser()
            user.deactivate()
            assertThat(user.userState()).isEqualTo(UserState.INACTIVE)
        }

        @Test
        @DisplayName("ban() 호출 시 상태가 BANNED가 된다")
        fun becomesBanned() {
            val user = createUser()
            user.ban()
            assertThat(user.userState()).isEqualTo(UserState.BANNED)
        }

        @Test
        @DisplayName("activate() 호출 시 상태가 ACTIVE가 된다")
        fun becomesActive() {
            val user = createUser(userState = UserState.INACTIVE)
            user.activate()
            assertThat(user.userState()).isEqualTo(UserState.ACTIVE)
        }
    }

    @Nested
    @DisplayName("isActive()는")
    inner class IsActive {
        @Test
        @DisplayName("ACTIVE 상태일 경우 true를 반환한다")
        fun returnsTrueIfActive() {
            val user = createUser(userState = UserState.ACTIVE)
            assertThat(user.isActive()).isTrue()
        }

        @Test
        @DisplayName("ACTIVE 상태가 아닐 경우 false를 반환한다")
        fun returnsFalseIfNotActive() {
            val user = createUser(userState = UserState.BANNED)
            assertThat(user.isActive()).isFalse()
        }
    }

    private fun createUser(
        name: String = "장순님",
        profile: ProfileImage = ProfileImage.BASIC,
        userState: UserState = UserState.ACTIVE,
    ): User {
        return User(
            name = UserName(name),
            profileImage = profile,
            userState = userState,
        )
    }
}
