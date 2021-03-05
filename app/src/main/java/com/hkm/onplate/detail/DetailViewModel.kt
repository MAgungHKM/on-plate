package com.hkm.onplate.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hkm.onplate.core.data.Resource
import com.hkm.onplate.core.domain.model.Recipe
import com.hkm.onplate.core.domain.usecase.OnPlateUseCase
import kotlinx.coroutines.runBlocking

class DetailViewModel constructor(private val mOnPlateUseCase: OnPlateUseCase) : ViewModel() {
    private var recipeId: String = ""
    private var detail: LiveData<Resource<Recipe>>? = null

    fun setRecipeId(recipeId: String) {
        this.recipeId = recipeId
    }

    fun getRecipeDetail(): LiveData<Resource<Recipe>> {
        if (detail == null || detail?.value == null) {
            detail = mOnPlateUseCase.getRecipeDetail(recipeId).asLiveData()
        }

        return detail as LiveData<Resource<Recipe>>
    }

    fun isFavorite() = mOnPlateUseCase.isFavorite(recipeId)

    fun setFavorite(): Boolean {
        var state: Boolean? = null
        if (detail != null) {
            val recipeResource = detail?.value
            if (recipeResource != null) {
                val recipe = recipeResource.data
                if (recipe != null) {
                    val newState = !isFavorite()
                    if (newState) runBlocking { mOnPlateUseCase.insertFavorite(recipe) }
                    else mOnPlateUseCase.deleteFavorite(recipe)
                    state = newState
                }
            }
        }
        return state as Boolean
    }
}