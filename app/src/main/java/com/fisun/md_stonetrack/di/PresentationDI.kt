package com.fisun.md_stonetrack.di

import com.fisun.md_stonetrack.domain.usecase.ChangePasswordUseCase
import com.fisun.md_stonetrack.domain.usecase.CheckAuthUseCase
import com.fisun.md_stonetrack.domain.usecase.CheckUsernameExistsUseCase
import com.fisun.md_stonetrack.domain.usecase.DeleteAccountUseCase
import com.fisun.md_stonetrack.domain.usecase.GetAccessTokenUseCase
import com.fisun.md_stonetrack.domain.usecase.GetCurrentUserByIdUseCase
import com.fisun.md_stonetrack.domain.usecase.GetCurrentUserUseCase
import com.fisun.md_stonetrack.domain.usecase.GetOrdersUseCase
import com.fisun.md_stonetrack.domain.usecase.LoginUseCase
import com.fisun.md_stonetrack.domain.usecase.LogoutUseCase
import com.fisun.md_stonetrack.domain.usecase.RegisterUseCase
import com.fisun.md_stonetrack.domain.usecase.ResetPasswordUseCase
import com.fisun.md_stonetrack.domain.usecase.SendFeedbackUseCase
import com.fisun.md_stonetrack.domain.usecase.UpdateOrderStatusUseCase
import com.fisun.md_stonetrack.domain.usecase.ValidateRegistrationFieldsUseCase
import com.fisun.md_stonetrack.presentation.client.account_settings_screen.AccountSettingsViewModel
import com.fisun.md_stonetrack.presentation.client.change_password.ChangePasswordViewModel
import com.fisun.md_stonetrack.presentation.client.feedback_screen.FeedbackViewModel
import com.fisun.md_stonetrack.presentation.client.history_detail_screen.HistoryDetailViewModel
import com.fisun.md_stonetrack.presentation.client.history_screen.HistoryViewModel
import com.fisun.md_stonetrack.presentation.client.order_detail_screen.OrderDetailViewModel
import com.fisun.md_stonetrack.presentation.client.order_screen.OrderViewModel
import com.fisun.md_stonetrack.presentation.client.profile_screen.ProfileViewModel
import com.fisun.md_stonetrack.presentation.courier.courier_account_settings_screen.CourierAccountSettingsViewModel
import com.fisun.md_stonetrack.presentation.courier.courier_change_password.CourierChangePasswordViewModel
import com.fisun.md_stonetrack.presentation.courier.courier_feedback_screen.CourierFeedbackViewModel
import com.fisun.md_stonetrack.presentation.courier.courier_history_detail_screen.CourierHistoryDetailViewModel
import com.fisun.md_stonetrack.presentation.courier.courier_history_screen.CourierHistoryViewModel
import com.fisun.md_stonetrack.presentation.courier.courier_order_detail_screen.CourierOrderDetailViewModel
import com.fisun.md_stonetrack.presentation.courier.courier_order_screen.CourierViewModel
import com.fisun.md_stonetrack.presentation.courier.courier_profile_screen.CourierProfileViewModel
import com.fisun.md_stonetrack.presentation.register_screen.RegistrationViewModel
import com.fisun.md_stonetrack.presentation.reset_password_screen.ResetPasswordViewModel
import com.fisun.md_stonetrack.presentation.sign_in_screen.SignInViewModel
import com.fisun.md_stonetrack.presentation.splash_screen.SplashViewModel
import com.fisun.md_stonetrack.presentation.utils.NotificationHelper
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { OrderViewModel(get(), get(), get(), get()) }
    viewModel { CourierViewModel(get(), get(), get(), get()) }
    viewModel { OrderDetailViewModel(get()) }
    viewModel { CourierOrderDetailViewModel(get(), get()) }
    viewModel { HistoryDetailViewModel(get()) }
    viewModel { CourierHistoryDetailViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { CourierProfileViewModel(get(), get(), get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { RegistrationViewModel(get(), get(), get()) }
    viewModel { HistoryViewModel(get(), get(), get()) }
    viewModel { CourierHistoryViewModel(get(), get(), get()) }
    viewModel { FeedbackViewModel(get(), get(), get(), get())}
    viewModel { CourierFeedbackViewModel(get(), get(), get(), get())}
    viewModel { AccountSettingsViewModel(get())}
    viewModel { CourierAccountSettingsViewModel(get())}
    viewModel { ChangePasswordViewModel(get(), get())}
    viewModel { CourierChangePasswordViewModel(get(), get())}
    viewModel { ResetPasswordViewModel(get())}

    factory { LoginUseCase(get()) }
    factory { GetOrdersUseCase(orderRepository = get(), authRepository = get()) }
    factory { GetCurrentUserUseCase(get()) }
    factory { GetCurrentUserByIdUseCase(get()) }
    factory { CheckAuthUseCase(get(), get()) }
    single { ValidateRegistrationFieldsUseCase() }
    single { RegisterUseCase(get()) }
    single { CheckUsernameExistsUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { SendFeedbackUseCase(get()) }
    single { UpdateOrderStatusUseCase(get()) }
    single { DeleteAccountUseCase(get()) }
    single { GetAccessTokenUseCase(get()) }
    single { ChangePasswordUseCase(get()) }
    single { ResetPasswordUseCase(get()) }

    single { NotificationHelper(androidContext()) }
}
