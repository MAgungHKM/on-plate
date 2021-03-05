@file:Suppress("unused")

package com.hkm.onplate

import android.app.Application
import com.hkm.onplate.core.di.databaseModule
import com.hkm.onplate.core.di.networkModule
import com.hkm.onplate.core.di.repositoryModule
import com.hkm.onplate.di.useCaseModule
import com.hkm.onplate.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

import org.koin.core.logger.Level

class OnPlate : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@OnPlate)
            modules(
                    listOf(
                            repositoryModule,
                            databaseModule,
                            networkModule,
                            useCaseModule,
                            viewModelModule,
                    )
            )
        }
    }
}