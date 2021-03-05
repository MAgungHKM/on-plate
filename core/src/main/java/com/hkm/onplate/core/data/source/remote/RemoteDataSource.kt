package com.hkm.onplate.core.data.source.remote

import android.util.Log
import com.hkm.onplate.core.data.source.remote.network.ApiConfig
import com.hkm.onplate.core.data.source.remote.network.ApiResponse
import com.hkm.onplate.core.data.source.remote.response.AutoCompleteResponse
import com.hkm.onplate.core.data.source.remote.response.RecipeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource {

    suspend fun getRandomRecipes(): Flow<ApiResponse<List<RecipeResponse>>> {
        //get data from remote api
        return flow {
            try {
                val client = ApiConfig.provideApiService()
                val response = client.getRandomRecipes()
                val dataArray = response.recipes
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.recipes))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMoreRecipes(): Flow<ApiResponse<List<RecipeResponse>>> {
        //get data from remote api
        return flow {
            try {
                val client = ApiConfig.provideApiService()
                val response = client.getMORERecipes()
                val dataArray = response.recipes
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.recipes))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getRecipeDetail(recipeId: String): Flow<ApiResponse<RecipeResponse>> {
        //get data from remote api
        return flow {
            try {
                val client = ApiConfig.provideApiService()
                val response = client.getRecipeDetail(recipeId)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAutoCompleteResult(query: String): Flow<ApiResponse<List<AutoCompleteResponse>>> {
        //get data from remote api
        return flow {
            try {
                val client = ApiConfig.provideApiService()
                val response = client.getAutoCompleteResult(query = query)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}
