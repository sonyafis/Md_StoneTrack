package com.example.md_stonetrack.data.di

import com.example.md_stonetrack.domain.usecase.LoginUseCase
import com.example.md_stonetrack.presentation.OrdersScreen.OrderViewModel
import com.example.md_stonetrack.presentation.SignInScreen.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { OrderViewModel(get()) }
    // Регистрируем ViewModel через viewModel()
    viewModel { AuthViewModel(get()) }

    // UseCase
    factory { LoginUseCase(get()) }
}
