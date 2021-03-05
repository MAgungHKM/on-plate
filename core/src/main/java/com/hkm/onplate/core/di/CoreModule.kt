package com.hkm.onplate.core.di

import androidx.room.Room
import com.hkm.onplate.core.data.OnPlateRepository
import com.hkm.onplate.core.data.source.local.LocalDataSource
import com.hkm.onplate.core.data.source.local.room.OnPlateDatabase
import com.hkm.onplate.core.data.source.remote.RemoteDataSource
import com.hkm.onplate.core.data.source.remote.network.ApiConfig.Companion.provideApiService
import com.hkm.onplate.core.domain.repository.IOnPlateRepository
import com.hkm.onplate.core.utils.AppExecutors
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { get<OnPlateDatabase>().onPlateDao() }
    single {
        Room.databaseBuilder(
                androidContext(),
                OnPlateDatabase::class.java, "OnPlate.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single { provideApiService() }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource() }
    single { AppExecutors() }
    factory<IOnPlateRepository> { OnPlateRepository(androidContext(), get(), get(), get()) }
}