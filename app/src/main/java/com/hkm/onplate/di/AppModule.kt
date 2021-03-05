package com.hkm.onplate.di

import com.hkm.onplate.MainViewModel
import com.hkm.onplate.core.domain.usecase.OnPlateInteractor
import com.hkm.onplate.core.domain.usecase.OnPlateUseCase
import com.hkm.onplate.detail.DetailViewModel
import com.hkm.onplate.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    single<OnPlateUseCase> { OnPlateInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}