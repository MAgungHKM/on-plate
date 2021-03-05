package com.hkm.onplate.favorites.di

import com.hkm.onplate.favorites.favorite.FavoriteViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoritesModule = module {
    viewModel { FavoriteViewModel(get()) }
}