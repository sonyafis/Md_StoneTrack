package com.example.md_stonetrack.di

import com.example.md_stonetrack.data.repository.FeedbackRepositoryImpl
import com.example.md_stonetrack.domain.repository.FeedbackRepository
import com.example.md_stonetrack.domain.usecase.GetOrdersUseCase
import com.example.md_stonetrack.domain.usecase.LogoutUseCase
import com.example.md_stonetrack.domain.usecase.ValidateRegistrationFieldsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetOrdersUseCase(get(), get()) }
    factory { LogoutUseCase(get()) }
    single { ValidateRegistrationFieldsUseCase() }
    single<FeedbackRepository> { FeedbackRepositoryImpl(get()) }
}
