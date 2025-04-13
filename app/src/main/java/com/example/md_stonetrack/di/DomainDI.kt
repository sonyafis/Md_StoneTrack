package com.example.md_stonetrack.data.di

import com.example.md_stonetrack.domain.usecase.GetOrdersUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetOrdersUseCase(get(), get()) }
}
