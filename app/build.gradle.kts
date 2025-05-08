plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("kotlin-kapt")
}

android {
    namespace = "com.fisun.md_stonetrack"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.fisun.md_stonetrack"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

}

dependencies {
    // Jetpack Compose BOM — рекомендуемый способ подключения (все версии синхронизированы)
    implementation(platform("androidx.compose:compose-bom:2025.01.00"))
    implementation("androidx.compose.compiler:compiler:1.5.10")
    // Основные компоненты Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Compose Activity
    implementation(libs.androidx.activity.compose)

    // Для работы с ViewModel в Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Интерактивный предпросмотр в Android Studio
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Для тестов Compose
    androidTestImplementation(libs.androidx.ui.test.junit4)

    //koin
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson) // Для преобразования JSON с использованием Gson
    implementation(libs.okhttp)
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    //RecyclerView (Отображение списков)
    implementation(libs.androidx.recyclerview)
    //ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // Room (для локальной базы данных)
    implementation(libs.androidx.room.runtime)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation(libs.androidx.room.ktx) // Coroutines поддержка
    // Navigation Compose
    implementation(libs.androidx.navigation.compose) // Актуальная версия для Compose
    // Material Icons Extended (для иконок)
    implementation(libs.androidx.material.icons.extended) // Совместима с вашим compose-bom
    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)
    // DataStore (Preferences)
    implementation(libs.androidx.datastore.preferences)
    implementation("com.google.accompanist:accompanist-swiperefresh:0.33.2-alpha")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}