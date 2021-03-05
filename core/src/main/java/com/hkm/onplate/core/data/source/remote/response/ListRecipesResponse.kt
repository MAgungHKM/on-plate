package com.hkm.onplate.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListRecipesResponse(

        @field:SerializedName("recipes")
        val recipes: List<RecipeResponse>
)


