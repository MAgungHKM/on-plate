package com.hkm.onplate.core.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.hkm.onplate.core.R
import com.hkm.onplate.core.data.source.local.LocalDataSource
import com.hkm.onplate.core.data.source.local.entity.FavoriteRecipeEntity
import com.hkm.onplate.core.data.source.remote.RemoteDataSource
import com.hkm.onplate.core.data.source.remote.network.ApiResponse
import com.hkm.onplate.core.data.source.remote.response.AutoCompleteResponse
import com.hkm.onplate.core.data.source.remote.response.RecipeResponse
import com.hkm.onplate.core.domain.model.Recipe
import com.hkm.onplate.core.domain.repository.IOnPlateRepository
import com.hkm.onplate.core.utils.AppExecutors
import com.hkm.onplate.core.utils.DataMapper
import com.hkm.onplate.core.utils.observeUntilCallback
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class OnPlateRepository constructor(
    private val context: Context,
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IOnPlateRepository {
    override fun getAllRecipes(): Flow<Resource<List<Recipe>>> {
        appExecutors.diskIO().execute { localDataSource.emptyRecipes() }
        return object : NetworkBoundResource<List<Recipe>, List<RecipeResponse>>() {
            override fun loadFromDB(): Flow<List<Recipe>> {
                return localDataSource.getAllRecipes().map {
                    DataMapper.mapRecipeEntitiesToItsDomain(it)
                }
            }

            override fun shouldFetch(data: List<Recipe>?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<List<RecipeResponse>>> =
                remoteDataSource.getRandomRecipes()

            override suspend fun saveCallResult(data: List<RecipeResponse>?) {
                if (data != null) {
                    val recipeList = DataMapper.mapRecipeResponsesToItsEntities(data)
                    localDataSource.insertRecipes(recipeList)
                }
            }
        }.asFlow()
    }

    override suspend fun loadMoreRecipes() {
        val flow = object : NetworkBoundResource<List<Recipe>, List<RecipeResponse>>() {
            override fun loadFromDB(): Flow<List<Recipe>> {
                return localDataSource.getAllRecipes().map {
                    DataMapper.mapRecipeEntitiesToItsDomain(it)
                }
            }

            override fun shouldFetch(data: List<Recipe>?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<List<RecipeResponse>>> =
                    remoteDataSource.getMoreRecipes()

            override suspend fun saveCallResult(data: List<RecipeResponse>?) {
                if (data != null) {
                    val recipeList = DataMapper.mapRecipeResponsesToItsEntities(data)
                    localDataSource.insertRecipes(recipeList)
                }
            }
        }.asFlow()

        var callback: LiveData<Boolean>? = null
        flow.asLiveData().observeUntilCallback({ recipes ->
            when (recipes) {
                is Resource.Loading -> {
                    toast(R.string.loading_more)
                    Log.d("loadMoreRecipe(): ", "Status: Loading")
                }
                is Resource.Success -> {
                    val liveCallback: MutableLiveData<Boolean>? = null
                    liveCallback?.value = true
                    callback = liveCallback
                    toast(R.string.loading_success)
                    Log.d("loadMoreRecipe(): ", "Status: Success")
                }
                is Resource.Error -> {
                    toast(R.string.loading_failed)
                    Log.d("loadMoreRecipe(): ", "Status: Error")
                }
            }
        }, callback)
    }

    override fun getAllFavorites(): Flow<Resource<List<Recipe>>> =
            object : NetworkBoundResource<List<Recipe>, List<RecipeResponse>>() {
                override fun loadFromDB(): Flow<List<Recipe>> {
                    return localDataSource.getAllFavorites().map {
                        DataMapper.mapRecipeEntitiesToItsDomain(
                                DataMapper.mapFavoriteRecipeEntitiesToRecipeEntity(it)
                        )
                    }
                }

                override fun shouldFetch(data: List<Recipe>?): Boolean =
                        data == null || data.isEmpty()

                override suspend fun createCall(): Flow<ApiResponse<List<RecipeResponse>>> =
                        remoteDataSource.getRandomRecipes()

                override suspend fun saveCallResult(data: List<RecipeResponse>?) {
                    if (data != null) {
                        val recipeList = DataMapper.mapRecipeResponsesToItsEntities(data)
                        localDataSource.insertRecipes(recipeList)
                    }
                }
            }.asFlow()

    override fun getRecipeDetail(recipeId: String): Flow<Resource<Recipe>> =
            object : NetworkBoundResource<Recipe, RecipeResponse>() {
                override fun loadFromDB(): Flow<Recipe> =
                        localDataSource.getRecipeDetail(recipeId).map {
                            DataMapper.mapRecipeEntityToItsDomain(it)
                        }

                override fun shouldFetch(data: Recipe?): Boolean =
                        data == null || data.summary == "" || data.ingredients.isEmpty() || data.instructions.isEmpty() || data.score == ""

                override suspend fun createCall(): Flow<ApiResponse<RecipeResponse>> =
                        remoteDataSource.getRecipeDetail(recipeId)

                override suspend fun saveCallResult(data: RecipeResponse?) {
                    if (data != null) {
                        val recipe = DataMapper.mapRecipeResponseToItsEntity(data)
                        appExecutors.diskIO().execute { localDataSource.insertRecipe(recipe) }
                    }
                }
            }.asFlow()

    override fun getAutoCompleteResult(query: String): Flow<Resource<List<Recipe>>> =
            object : NetworkBoundResource<List<Recipe>, List<AutoCompleteResponse>>() {
                override fun loadFromDB(): Flow<List<Recipe>> {
                    return localDataSource.getAllAutoCompleteResult().map {
                        DataMapper.mapRecipeEntitiesToItsDomain(
                                DataMapper.mapAutoCompleteEntitiesToItsRecipeEntity(it)
                        )
                    }
                }

                override fun shouldFetch(data: List<Recipe>?): Boolean = true

                override suspend fun createCall(): Flow<ApiResponse<List<AutoCompleteResponse>>> =
                        remoteDataSource.getAutoCompleteResult(query)

                override suspend fun saveCallResult(data: List<AutoCompleteResponse>?) {
                    if (data != null) {
                        val resultList = DataMapper.mapAutoCompleteResponseToItsEntities(data)
                        localDataSource.insertAutoCompleteResults(resultList)
                    }
                }
            }.asFlow()

    override fun getFavoriteDetail(recipeId: String): Flow<Resource<Recipe>> =
            object : NetworkBoundResource<Recipe, RecipeResponse>() {
                override fun loadFromDB(): Flow<Recipe> =
                        localDataSource.getFavoriteDetail(recipeId).map {
                            DataMapper.mapRecipeEntityToItsDomain(
                                    DataMapper.mapFavoriteRecipeEntityToRecipeEntity(it as FavoriteRecipeEntity)
                            )
                        }

                override fun shouldFetch(data: Recipe?): Boolean =
                        data == null || data.summary == "" || data.ingredients.isEmpty() || data.instructions.isEmpty() || data.score == ""

                override suspend fun createCall(): Flow<ApiResponse<RecipeResponse>> =
                        remoteDataSource.getRecipeDetail(recipeId)

                override suspend fun saveCallResult(data: RecipeResponse?) {
                    if (data != null) {
                        val recipe = DataMapper.mapRecipeResponseToItsEntity(data)
                        localDataSource.insertRecipe(recipe)
                    }
                }
            }.asFlow()

    override fun isFavorite(recipeId: String): Boolean {
        val recipe = localDataSource.getFavoriteDetail(recipeId)
        val data = runBlocking { recipe.first() }
        return data != null
    }

    override fun insertFavorite(recipe: Recipe) = appExecutors.diskIO().execute {
        localDataSource.insertFavorite(
            DataMapper.mapRecipeEntityToFavoriteRecipeEntity(
                DataMapper.mapRecipeDomainToItsEntity(recipe)
            )
        )
    }

    override fun insertRecipe(recipe: Recipe) = appExecutors.diskIO().execute {
        localDataSource.insertRecipe(DataMapper.mapRecipeDomainToItsEntity(recipe))
    }

    override fun deleteFavorite(recipe: Recipe) = appExecutors.diskIO().execute { localDataSource.deleteFavorite(recipe.recipeId) }
    override fun deleteAllFavorite() = appExecutors.diskIO().execute { localDataSource.deleteAllFavorite() }
    override fun emptyRecipes() = appExecutors.diskIO().execute { localDataSource.emptyRecipes() }
    override fun emptyAutoCompleteResults() = appExecutors.diskIO().execute { localDataSource.emptyAutoCompleteResults() }

    private fun toast(stringId: Int) = Toast.makeText(context, context.getString(stringId), Toast.LENGTH_SHORT).show()
}