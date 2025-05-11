package com.annasolox.kipon.ui

import com.annasolox.kipon.ui.viewmodels.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }
}