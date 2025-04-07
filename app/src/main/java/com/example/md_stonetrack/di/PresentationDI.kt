package com.example.md_stonetrack.data.di

import com.example.md_stonetrack.presentation.OrdersScreen.OrderViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { OrderViewModel(get()) }
}
