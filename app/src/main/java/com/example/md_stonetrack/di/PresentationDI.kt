package com.example.md_stonetrack.di

import com.example.md_stonetrack.data.repository.RegistrationRepositoryImpl
import com.example.md_stonetrack.domain.repository.RegistrationRepository
import com.example.md_stonetrack.domain.usecase.CheckAuthUseCase
import com.example.md_stonetrack.domain.usecase.CheckUsernameExistsUseCase
import com.example.md_stonetrack.domain.usecase.DeleteAccountUseCase
import com.example.md_stonetrack.domain.usecase.GetCurrentUserUseCase
import com.example.md_stonetrack.domain.usecase.GetOrdersUseCase
import com.example.md_stonetrack.domain.usecase.LoginUseCase
import com.example.md_stonetrack.domain.usecase.LogoutUseCase
import com.example.md_stonetrack.domain.usecase.RegisterUseCase
import com.example.md_stonetrack.domain.usecase.SendFeedbackUseCase
import com.example.md_stonetrack.domain.usecase.UpdateOrderStatusUseCase
import com.example.md_stonetrack.domain.usecase.ValidateRegistrationFieldsUseCase
import com.example.md_stonetrack.presentation.client.feedback_screen.FeedbackViewModel
import com.example.md_stonetrack.presentation.client.history_detail_screen.HistoryDetailViewModel
import com.example.md_stonetrack.presentation.client.history_screen.HistoryViewModel
import com.example.md_stonetrack.presentation.client.order_detail_screen.OrderDetailViewModel
import com.example.md_stonetrack.presentation.client.order_screen.OrderViewModel
import com.example.md_stonetrack.presentation.client.profile_screen.ProfileViewModel
import com.example.md_stonetrack.presentation.courier.courier_history_detail_screen.CourierHistoryDetailViewModel
import com.example.md_stonetrack.presentation.courier.courier_history_screen.CourierHistoryViewModel
import com.example.md_stonetrack.presentation.courier.courier_order_detail_screen.CourierOrderDetailViewModel
import com.example.md_stonetrack.presentation.courier.courier_order_screen.CourierViewModel
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
    viewModel { CourierViewModel(get(), get(), get(), get()) }
    viewModel { OrderDetailViewModel(get()) }
    viewModel { CourierOrderDetailViewModel(get(), get()) }
    viewModel { HistoryDetailViewModel(get()) }
    viewModel { CourierHistoryDetailViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { RegistrationViewModel(get(), get(), get()) }
    viewModel { HistoryViewModel(get(), get(), get()) }
    viewModel { CourierHistoryViewModel(get(), get(), get()) }
    viewModel { FeedbackViewModel(get(), get(), get())}
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
    single { SendFeedbackUseCase(get()) }
    single { UpdateOrderStatusUseCase(get()) }
    single { DeleteAccountUseCase(get()) }

    single { NotificationHelper(androidContext()) }
}
