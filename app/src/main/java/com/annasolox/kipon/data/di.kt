package com.annasolox.kipon.data

import com.annasolox.kipon.data.api.service.AccountService
import com.annasolox.kipon.data.api.service.AccountServiceImpl
import com.annasolox.kipon.data.api.service.AuthService
import com.annasolox.kipon.data.api.service.AuthServiceImpl
import com.annasolox.kipon.data.api.service.UserService
import com.annasolox.kipon.data.api.service.UserServiceImpl
import com.annasolox.kipon.data.repository.AccountRepository
import com.annasolox.kipon.data.repository.AuthRepository
import com.annasolox.kipon.data.repository.UserRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    //services
    single<AuthService> { AuthServiceImpl(get(), get()) }
    single<UserService> { UserServiceImpl(get()) }
    single<AccountService> { AccountServiceImpl(get()) }
    //repositories
    factoryOf(::AuthRepository)
    factoryOf(::UserRepository)
    factoryOf(::AccountRepository)
}