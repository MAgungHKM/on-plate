package com.hkm.onplate.core.data.source.remote.network

import com.hkm.onplate.core.BuildConfig
import com.hkm.onplate.core.data.source.remote.response.AutoCompleteResponse
import com.hkm.onplate.core.data.source.remote.response.ListRecipesResponse
import com.hkm.onplate.core.data.source.remote.response.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
            @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
            @Query("number") number: Int = 10,
    ): ListRecipesResponse

    @GET("recipes/random")
    suspend fun getMORERecipes(
            @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
            @Query("number") number: Int = 10,
    ): ListRecipesResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeDetail(
            @Path("id") id: String,
            @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
    ): RecipeResponse

    @GET("recipes/autocomplete")
    suspend fun getAutoCompleteResult(
            @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
            @Query("number") number: Int = 5,
            @Query("query") query: String = "",
    ): List<AutoCompleteResponse>

}