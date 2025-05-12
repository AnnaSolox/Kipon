package com.annasolox.kipon.core.utils.mappers

import com.annasolox.kipon.data.api.models.request.create.ProfileCreate
import com.annasolox.kipon.data.api.models.request.create.UserCreate
import com.annasolox.kipon.data.api.models.response.UserProfileResponse
import com.annasolox.kipon.data.api.models.response.UserResponse
import com.annasolox.kipon.ui.models.Profile
import com.annasolox.kipon.ui.models.UserHomeScreen
import com.annasolox.kipon.ui.models.UserProfileScreen
import java.time.LocalDate

object UserMapper {
    fun toUserCreate(
        name: String,
        password: String,
        email: String,
        completeName: String,
        phone: String,
        address: String,
        photo: String? = null
    ): UserCreate {
        val profile = ProfileCreate(
            completeName = completeName,
            telephone = phone,
            address = address,
            photo = photo
        )
        return UserCreate(
            name = name,
            password = password,
            email = email,
            profile = profile,
            registerDate = LocalDate.now(),
        )
    }

    fun toUserHomeScreen(
        userResponse: UserResponse
    ): UserHomeScreen {
        return UserHomeScreen(
            userName = userResponse.name,
            photoUrl = userResponse.profile.photo ?: "",
            accounts = userResponse.accountRole.map { account ->
                AccountMapper.toAccountOverviewFromRole(account)
            }
        )
    }

    fun toUserProfileScreen(
        userResponse: UserResponse
    ): UserProfileScreen {
        return UserProfileScreen(
            id = userResponse.id,
            name = userResponse.name,
            email = userResponse.email,
            profile = toProfileUI(userResponse.profile)
            )
    }

    fun toProfileUI(
     userProfileResponse: UserProfileResponse
    ): Profile {
        return Profile(
            id = userProfileResponse.id,
            completeName = userProfileResponse.completeName,
            telephone = userProfileResponse.telephone,
            address = userProfileResponse.address,
            photo = userProfileResponse.photo ?: ""
        )
    }
}