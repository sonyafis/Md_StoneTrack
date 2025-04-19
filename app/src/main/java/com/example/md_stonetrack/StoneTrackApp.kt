package com.example.md_stonetrack

import android.app.Application
import com.example.md_stonetrack.di.dataModule
import com.example.md_stonetrack.di.domainModule
import com.example.md_stonetrack.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class StoneTrackApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@StoneTrackApp)
            modules(
                dataModule,
                domainModule,
                presentationModule
            )
        }
    }
}
