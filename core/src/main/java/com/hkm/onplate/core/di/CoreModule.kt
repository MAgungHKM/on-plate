package com.hkm.onplate.core.di

import androidx.room.Room
import com.hkm.onplate.core.data.OnPlateRepository
import com.hkm.onplate.core.data.source.local.LocalDataSource
import com.hkm.onplate.core.data.source.local.room.OnPlateDatabase
import com.hkm.onplate.core.data.source.remote.RemoteDataSource
import com.hkm.onplate.core.data.source.remote.network.ApiConfig.Companion.provideApiService
import com.hkm.onplate.core.domain.repository.IOnPlateRepository
import com.hkm.onplate.core.utils.AppExecutors
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { get<OnPlateDatabase>().onPlateDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("MAgungHKM/on-plate".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            OnPlateDatabase::class.java, "OnPlate.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single { provideApiService() }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource() }
    single { AppExecutors() }
    single<IOnPlateRepository> { OnPlateRepository(androidContext(), get(), get(), get()) }
}