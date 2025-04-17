package com.example.md_stonetrack.data.di

import androidx.lifecycle.viewModelScope
import com.example.md_stonetrack.data.repository.RegistrationRepositoryImpl
import com.example.md_stonetrack.domain.repository.RegistrationRepository
import com.example.md_stonetrack.domain.usecase.CheckAuthUseCase
import com.example.md_stonetrack.domain.usecase.CheckUsernameExistsUseCase
import com.example.md_stonetrack.domain.usecase.GetCurrentUserUseCase
import com.example.md_stonetrack.domain.usecase.GetOrdersUseCase
import com.example.md_stonetrack.domain.usecase.LoginUseCase
import com.example.md_stonetrack.domain.usecase.RegisterUseCase
import com.example.md_stonetrack.domain.usecase.ValidateRegistrationFieldsUseCase
import com.example.md_stonetrack.presentation.OrderDetailScreen.OrderDetailView
import com.example.md_stonetrack.presentation.OrderDetailScreen.OrderDetailViewModel
import com.example.md_stonetrack.presentation.OrdersScreen.OrderViewModel
import com.example.md_stonetrack.presentation.ProfileScreen.ProfileViewModel
import com.example.md_stonetrack.presentation.RegisterScreen.RegistrationViewModel
import com.example.md_stonetrack.presentation.SignInScreen.SignInViewModel
import com.example.md_stonetrack.presentation.SplashScreen.SplashViewModel
import com.example.md_stonetrack.presentation.utils.NotificationHelper
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    // ViewModel
    viewModel { OrderViewModel(get(), get(), get()) }
    viewModel { OrderDetailViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { RegistrationViewModel(get(), get(), get()) }
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

    single { NotificationHelper(androidContext()) }
}
