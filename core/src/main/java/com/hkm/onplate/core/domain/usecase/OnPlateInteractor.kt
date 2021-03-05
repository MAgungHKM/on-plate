package com.hkm.onplate.core.domain.usecase

import com.hkm.onplate.core.domain.model.Recipe
import com.hkm.onplate.core.domain.repository.IOnPlateRepository

class OnPlateInteractor(private val mIOnPlateRepository: IOnPlateRepository) : OnPlateUseCase {
    override fun getAllRecipes() = mIOnPlateRepository.getAllRecipes()
    override suspend fun loadMoreRecipes() = mIOnPlateRepository.loadMoreRecipes()
    override fun getAllFavorites() = mIOnPlateRepository.getAllFavorites()
    override fun getRecipeDetail(recipeId: String) = mIOnPlateRepository.getRecipeDetail(recipeId)
    override fun getAutoCompleteResult(query: String) = mIOnPlateRepository.getAutoCompleteResult(query)
    override fun getFavoriteDetail(recipeId: String) = mIOnPlateRepository.getFavoriteDetail(recipeId)
    override fun isFavorite(recipeId: String) = mIOnPlateRepository.isFavorite(recipeId)
    override fun insertFavorite(recipe: Recipe) = mIOnPlateRepository.insertFavorite(recipe)
    override fun insertRecipe(recipe: Recipe) = mIOnPlateRepository.insertRecipe(recipe)
    override fun deleteFavorite(recipe: Recipe) = mIOnPlateRepository.deleteFavorite(recipe)
    override fun deleteAllFavorite() = mIOnPlateRepository.deleteAllFavorite()
    override fun emptyRecipes() = mIOnPlateRepository.emptyRecipes()
    override fun emptyAutoCompleteResults() = mIOnPlateRepository.emptyAutoCompleteResults()
}