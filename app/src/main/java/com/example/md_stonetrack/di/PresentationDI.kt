package com.example.md_stonetrack.di

import com.example.md_stonetrack.data.repository.RegistrationRepositoryImpl
import com.example.md_stonetrack.domain.repository.RegistrationRepository
import com.example.md_stonetrack.domain.usecase.CheckAuthUseCase
import com.example.md_stonetrack.domain.usecase.CheckUsernameExistsUseCase
import com.example.md_stonetrack.domain.usecase.GetCurrentUserUseCase
import com.example.md_stonetrack.domain.usecase.GetOrdersUseCase
import com.example.md_stonetrack.domain.usecase.LoginUseCase
import com.example.md_stonetrack.domain.usecase.LogoutUseCase
import com.example.md_stonetrack.domain.usecase.RegisterUseCase
import com.example.md_stonetrack.domain.usecase.ValidateRegistrationFieldsUseCase
import com.example.md_stonetrack.presentation.history_detail_screen.HistoryDetailViewModel
import com.example.md_stonetrack.presentation.history_screen.HistoryViewModel
import com.example.md_stonetrack.presentation.order_detail_screen.OrderDetailViewModel
import com.example.md_stonetrack.presentation.order_screen.OrderViewModel
import com.example.md_stonetrack.presentation.profile_screen.ProfileViewModel
import com.example.md_stonetrack.presentation.register_screen.RegistrationViewModel
import com.example.md_stonetrack.presentation.sign_in_screen.SignInViewModel
import com.example.md_stonetrack.presentation.splash_screen.SplashViewModel
import com.example.md_stonetrack.presentation.utils.NotificationHelper
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    // ViewModel
    viewModel { OrderViewModel(get(), get(), get(), get()) }
    viewModel { OrderDetailViewModel(get()) }
    viewModel { HistoryDetailViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { RegistrationViewModel(get(), get(), get()) }
    viewModel { HistoryViewModel(get(), get(), get()) }
    //Repository
    single<RegistrationRepository> { RegistrationRepositoryImpl(get()) }
    // UseCase
    factory { LoginUseCase(get()) }
    factory { GetOrdersUseCase(orderRepository = get(), authRepository = get()) }
    factory { GetCurrentUserUseCase(get()) }
    factory { CheckAuthUseCase(get(), get()) }
    single { ValidateRegistrationFieldsUseCase() }
    single { RegisterUseCase(get()) }
    single { CheckUsernameExistsUseCase(get()) }
    single { LogoutUseCase(get()) }

    single { NotificationHelper(androidContext()) }
}
