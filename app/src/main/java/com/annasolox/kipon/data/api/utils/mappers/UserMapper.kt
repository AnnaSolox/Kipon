package com.annasolox.kipon.data.api.utils.mappers

import com.annasolox.kipon.data.api.models.request.create.ProfileCreate
import com.annasolox.kipon.data.api.models.request.create.UserCreate
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
    ) : UserCreate{
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
}