package com.example.md_stonetrack.data.di

import com.example.md_stonetrack.data.api.ApiService
import com.example.md_stonetrack.data.repository.OrderRepositoryImpl
import com.example.md_stonetrack.domain.repository.OrderRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/") // ✅ Эмулятор -> localhost
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    single<OrderRepository> { OrderRepositoryImpl(get()) }
}
