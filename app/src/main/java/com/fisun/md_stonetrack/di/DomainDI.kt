package com.fisun.md_stonetrack.di

import com.fisun.md_stonetrack.data.repository.FeedbackRepositoryImpl
import com.fisun.md_stonetrack.domain.repository.FeedbackRepository
import com.fisun.md_stonetrack.domain.usecase.GetOrdersUseCase
import com.fisun.md_stonetrack.domain.usecase.LogoutUseCase
import com.fisun.md_stonetrack.domain.usecase.ValidateRegistrationFieldsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetOrdersUseCase(get(), get()) }
    factory { LogoutUseCase(get()) }
    single { ValidateRegistrationFieldsUseCase() }
    single<FeedbackRepository> { FeedbackRepositoryImpl(get()) }
}
