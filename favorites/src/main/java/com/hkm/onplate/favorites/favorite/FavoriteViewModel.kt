package com.hkm.onplate.favorites.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hkm.onplate.core.domain.model.Recipe
import com.hkm.onplate.core.domain.usecase.OnPlateUseCase

class FavoriteViewModel(private val mOnPlateUseCase: OnPlateUseCase) : ViewModel() {
    val favorites = mOnPlateUseCase.getAllFavorites().asLiveData()

    fun deleteAllFavorite() = mOnPlateUseCase.deleteAllFavorite()

    fun insertRecipe(recipe: Recipe) = mOnPlateUseCase.insertRecipe(recipe)
}