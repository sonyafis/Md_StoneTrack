package com.example.md_stonetrack.data.di

import androidx.lifecycle.viewModelScope
import com.example.md_stonetrack.data.repository.RegistrationRepositoryImpl
import com.example.md_stonetrack.domain.repository.RegistrationRepository
import com.example.md_stonetrack.domain.usecase.CheckAuthUseCase
import com.example.md_stonetrack.domain.usecase.LoginUseCase
import com.example.md_stonetrack.presentation.OrdersScreen.OrderViewModel
import com.example.md_stonetrack.presentation.RegisterScreen.RegistrationViewModel
import com.example.md_stonetrack.presentation.SignInScreen.SignInViewModel
import com.example.md_stonetrack.presentation.SplashScreen.SplashViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { OrderViewModel(get(), get()) }
    // Регистрируем ViewModel через viewModel()
    viewModel { SignInViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    // Добавляем фабрику для CheckAuthUseCase
    factory {
        CheckAuthUseCase(
            authRepository = get(),
            userDao = get()
        )
    }
    single<RegistrationRepository> { RegistrationRepositoryImpl(get()) }
    viewModel { RegistrationViewModel(get()) }

    // UseCase
    factory { LoginUseCase(get()) }
}
