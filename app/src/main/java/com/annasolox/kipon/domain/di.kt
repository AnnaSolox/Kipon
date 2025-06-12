package com.annasolox.kipon.domain

import com.annasolox.kipon.domain.account.AddUserToAccountUseCase
import com.annasolox.kipon.domain.account.CreateAccountUseCase
import com.annasolox.kipon.domain.account.CreateContributionUseCase
import com.annasolox.kipon.domain.account.GetAccountUseCase
import com.annasolox.kipon.domain.account.UpdateAccountUseCase
import com.annasolox.kipon.domain.auth.ClearTokenUseCase
import com.annasolox.kipon.domain.auth.LoginUseCase
import com.annasolox.kipon.domain.auth.RegisterUseCase
import com.annasolox.kipon.domain.auth.SaveTokenUseCase
import com.annasolox.kipon.domain.auth.SaveUserNameUSeCase
import com.annasolox.kipon.domain.image.UploadImageUseCase
import com.annasolox.kipon.domain.user.GetUserUseCase
import com.annasolox.kipon.domain.user.SearchUserUseCase
import com.annasolox.kipon.domain.user.UpdateUserUseCase
import kotlinx.coroutines.sync.Mutex
import org.koin.dsl.module

val domainModule = module {
    single { Mutex() }

    factory { UpdateUserUseCase(get()) }
    factory { GetUserUseCase(get(), get()) }
    factory { UploadImageUseCase(get()) }
    factory { SearchUserUseCase(get()) }
    factory { AddUserToAccountUseCase(get()) }
    factory { CreateAccountUseCase(get(), get()) }
    factory { CreateContributionUseCase(get(), get(), get()) }
    factory { GetAccountUseCase(get(), get()) }
    factory { UpdateAccountUseCase(get()) }
    factory { ClearTokenUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { SaveTokenUseCase(get()) }
    factory { SaveUserNameUSeCase(get()) }
}