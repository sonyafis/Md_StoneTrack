package com.example.md_stonetrack.di

import com.example.md_stonetrack.domain.usecase.GetOrdersUseCase
import com.example.md_stonetrack.domain.usecase.LogoutUseCase
import com.example.md_stonetrack.domain.usecase.ValidateRegistrationFieldsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetOrdersUseCase(get(), get()) }
    factory { LogoutUseCase(get()) }
    single { ValidateRegistrationFieldsUseCase() }
}
