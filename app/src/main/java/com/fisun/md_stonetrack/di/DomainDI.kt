package com.fisun.md_stonetrack.di

import com.fisun.md_stonetrack.domain.repository.AuthRepository
import com.fisun.md_stonetrack.domain.repository.FeedbackRepository
import com.fisun.md_stonetrack.domain.repository.OrderRepository
import com.fisun.md_stonetrack.domain.repository.RegistrationRepository
import com.fisun.md_stonetrack.domain.usecase.ChangePasswordUseCase
import com.fisun.md_stonetrack.domain.usecase.CheckAuthUseCase
import com.fisun.md_stonetrack.domain.usecase.CheckEmailExistsUseCase
import com.fisun.md_stonetrack.domain.usecase.CheckUsernameExistsUseCase
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
import org.koin.dsl.module

val domainModule = module {
    factory { GetOrdersUseCase(get(), get()) }
    factory { LoginUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
    factory { GetCurrentUserByIdUseCase(get()) }
    factory { CheckAuthUseCase(get(), get()) }
    factory { ValidateRegistrationFieldsUseCase() }
    factory { RegisterUseCase(get()) }
    factory { CheckUsernameExistsUseCase(get()) }
    factory { CheckEmailExistsUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { SendFeedbackUseCase(get()) }
    factory { UpdateOrderStatusUseCase(get()) }
    factory { GetAccessTokenUseCase(get()) }
    factory { ChangePasswordUseCase(get()) }
    factory { ResetPasswordUseCase(get()) }
}
