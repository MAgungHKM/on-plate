package com.hkm.onplate.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hkm.onplate.core.data.source.local.entity.AutoCompleteEntity
import com.hkm.onplate.core.data.source.local.entity.FavoriteRecipeEntity
import com.hkm.onplate.core.data.source.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OnPlateDao {

    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): Flow<List<FavoriteRecipeEntity>>

    @Query("SELECT * FROM autocomplete")
    fun getAllAutoCompleteResult(): Flow<List<AutoCompleteEntity>>

    @Query("SELECT * FROM recipe WHERE recipeId = :recipeId")
    fun getRecipeDetail(recipeId: String): Flow<RecipeEntity>

    @Query("SELECT * FROM favorite WHERE recipeId = :recipeId")
    fun getFavoriteDetail(recipeId: String): Flow<FavoriteRecipeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipe: List<RecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutoCompleteResults(recipe: List<AutoCompleteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favoriteRecipe: FavoriteRecipeEntity)

    @Query("DELETE FROM favorite WHERE recipeId = :recipeId")
    fun deleteFavorite(recipeId: String)

    @Query("DELETE FROM favorite")
    fun deleteAllFavorite()

    @Query("DELETE FROM recipe")
    fun emptyRecipes()

    @Query("DELETE FROM autocomplete")
    fun emptyAutoCompleteResults()
}
