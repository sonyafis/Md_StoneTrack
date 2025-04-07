plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}

android {
    namespace = "com.example.md_stonetrack"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.md_stonetrack"
        minSdk = 24
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
    //RecyclerView (Отображение списков)
    implementation(libs.androidx.recyclerview)
    //ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}