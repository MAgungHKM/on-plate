package com.hkm.onplate.core.domain.usecase

import com.hkm.onplate.core.data.Resource
import com.hkm.onplate.core.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface OnPlateUseCase {
    fun getAllRecipes(): Flow<Resource<List<Recipe>>>
    suspend fun loadMoreRecipes()
    fun getAllFavorites(): Flow<Resource<List<Recipe>>>
    fun getRecipeDetail(recipeId: String): Flow<Resource<Recipe>>
    fun getAutoCompleteResult(query: String): Flow<Resource<List<Recipe>>>
    fun getFavoriteDetail(recipeId: String): Flow<Resource<Recipe>>
    fun isFavorite(recipeId: String): Boolean
    fun insertFavorite(recipe: Recipe)
    fun insertRecipe(recipe: Recipe)
    fun deleteFavorite(recipe: Recipe)
    fun deleteAllFavorite()
    fun emptyRecipes()
    fun emptyAutoCompleteResults()
}