package com.example.md_stonetrack.di

import com.example.md_stonetrack.data.repository.AuthRepositoryImpl
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.md_stonetrack.data.api.ApiService
import com.example.md_stonetrack.data.api.RetrofitClient
import com.example.md_stonetrack.data.db.AppDatabase
import com.example.md_stonetrack.data.repository.OrderRepositoryImpl
import com.example.md_stonetrack.data.security.CryptoManager
import com.example.md_stonetrack.domain.repository.AuthRepository
import com.example.md_stonetrack.domain.repository.OrderRepository
import com.example.md_stonetrack.domain.usecase.LoginUseCase
import com.example.md_stonetrack.presentation.sign_in_screen.SignInViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

val dataModule = module {

    // ApiService
    single<ApiService> { RetrofitClient.apiService }

    // Room
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().userDao() }
    single { CryptoManager() }

    // DataStore
    single<DataStore<Preferences>> { androidContext().dataStore }

    // Репозитории
    single<AuthRepository> { AuthRepositoryImpl(get(), androidContext(), get()) }
    single<OrderRepository> { OrderRepositoryImpl(get()) }

    // UseCases
    factory { LoginUseCase(get()) }

    // ViewModels
    viewModelOf(::SignInViewModel)
}