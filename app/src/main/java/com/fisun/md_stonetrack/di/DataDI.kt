package com.fisun.md_stonetrack.di

import com.fisun.md_stonetrack.data.repository.AuthRepositoryImpl
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.fisun.md_stonetrack.data.api.ApiService
import com.fisun.md_stonetrack.data.api.RetrofitClient
import com.fisun.md_stonetrack.data.db.AppDatabase
import com.fisun.md_stonetrack.data.repository.FeedbackRepositoryImpl
import com.fisun.md_stonetrack.data.repository.OrderRepositoryImpl
import com.fisun.md_stonetrack.data.repository.RegistrationRepositoryImpl
import com.fisun.md_stonetrack.data.security.CryptoManager
import com.fisun.md_stonetrack.domain.repository.AuthRepository
import com.fisun.md_stonetrack.domain.repository.FeedbackRepository
import com.fisun.md_stonetrack.domain.repository.OrderRepository
import com.fisun.md_stonetrack.domain.repository.RegistrationRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

val dataModule = module {
    single<ApiService> { RetrofitClient.apiService }
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().userDao() }
    single { CryptoManager() }
    single<DataStore<Preferences>> { androidContext().dataStore }
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<OrderRepository> { OrderRepositoryImpl(get()) }
    single<FeedbackRepository> { FeedbackRepositoryImpl(get()) }
    single<RegistrationRepository> { RegistrationRepositoryImpl(get()) }
}