package com.hkm.onplate.core.data.source.local

import com.hkm.onplate.core.data.source.local.entity.AutoCompleteEntity
import com.hkm.onplate.core.data.source.local.entity.FavoriteRecipeEntity
import com.hkm.onplate.core.data.source.local.entity.RecipeEntity
import com.hkm.onplate.core.data.source.local.room.OnPlateDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource constructor(private val mOnPlateDao: OnPlateDao) {

    fun getAllRecipes(): Flow<List<RecipeEntity>> = mOnPlateDao.getAllRecipes()

    fun getAllFavorites(): Flow<List<FavoriteRecipeEntity>> = mOnPlateDao.getAllFavorites()

    fun getAllAutoCompleteResult(): Flow<List<AutoCompleteEntity>> = mOnPlateDao.getAllAutoCompleteResult()

    fun getRecipeDetail(recipeId: String): Flow<RecipeEntity> = mOnPlateDao.getRecipeDetail(recipeId)

    fun getFavoriteDetail(recipeId: String): Flow<FavoriteRecipeEntity?> = mOnPlateDao.getFavoriteDetail(recipeId)

    suspend fun insertRecipes(recipesList: List<RecipeEntity>) = mOnPlateDao.insertRecipes(recipesList)

    fun insertRecipe(recipe: RecipeEntity) = mOnPlateDao.insertRecipe(recipe)

    suspend fun insertAutoCompleteResults(resultList: List<AutoCompleteEntity>) = mOnPlateDao.insertAutoCompleteResults(resultList)

    fun insertFavorite(favoriteRecipe: FavoriteRecipeEntity) = mOnPlateDao.insertFavorite(favoriteRecipe)

    fun deleteFavorite(favoriteId: String) = mOnPlateDao.deleteFavorite(favoriteId)

    fun deleteAllFavorite() = mOnPlateDao.deleteAllFavorite()

    fun emptyRecipes() = mOnPlateDao.emptyRecipes()

    fun emptyAutoCompleteResults() = mOnPlateDao.emptyAutoCompleteResults()
}