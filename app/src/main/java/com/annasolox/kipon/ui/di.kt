package com.annasolox.kipon.ui

import com.annasolox.kipon.ui.viewmodels.AuthViewModel
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf( ::AuthViewModel )
    viewModelOf ( ::UserViewModel )

}