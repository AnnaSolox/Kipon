package com.annasolox.kipon

import android.content.Context
import com.annasolox.kipon.data.api.service.UserService
import com.annasolox.kipon.data.api.service.UserServiceImpl
import com.annasolox.kipon.data.repository.ImageUploadRepository
import com.annasolox.kipon.data.repository.UserRepository
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val testModule = module {
    single { createMockClient() }
    factoryOf(::UserRepository)
    factoryOf(::ImageUploadRepository)
    single<UserService> { UserServiceImpl(get()) }
    single { (UserViewModel(get(), get(), get())) }
    single { androidContext().getSharedPreferences("KiponPrefs", Context.MODE_PRIVATE) }
}