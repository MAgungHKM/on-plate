package com.hkm.onplate.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hkm.onplate.core.data.Resource
import com.hkm.onplate.core.domain.model.Recipe
import com.hkm.onplate.core.domain.usecase.OnPlateUseCase
import kotlinx.coroutines.runBlocking

class HomeViewModel constructor(private val mOnPlateUseCase: OnPlateUseCase) : ViewModel() {
    private var recipes: LiveData<Resource<List<Recipe>>>? = null
    private var searchResult: LiveData<Resource<List<Recipe>>>? = null

    fun search(query: String): LiveData<Resource<List<Recipe>>> {
        searchResult = mOnPlateUseCase.getAutoCompleteResult(query).asLiveData()

        return searchResult as LiveData<Resource<List<Recipe>>>
    }

    fun getRecipes(): LiveData<Resource<List<Recipe>>> {
        if (recipes == null || recipes?.value == null) {
            recipes = mOnPlateUseCase.getAllRecipes().asLiveData()
        }

        return recipes as LiveData<Resource<List<Recipe>>>
    }

    fun insertRecipe(recipe: Recipe) = mOnPlateUseCase.insertRecipe(recipe)

    fun loadMoreRecipes() = runBlocking { mOnPlateUseCase.loadMoreRecipes() }

    fun emptyAutoCompleteResults() = mOnPlateUseCase.emptyAutoCompleteResults()
}