package com.hkm.onplate.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RecipeResponse(

        @field:SerializedName("instructions")
        val instructions: String? = "",

        @field:SerializedName("analyzedInstructions")
        val analyzedInstructions: List<AnalyzedInstructionsItem> = listOf(),

        @field:SerializedName("title")
        val title: String? = "",

        @field:SerializedName("readyInMinutes")
        val readyInMinutes: Int = 0,

        @field:SerializedName("servings")
        val servings: Int = 0,

        @field:SerializedName("id")
        val id: Int = 0,

        @field:SerializedName("summary")
        val summary: String? = "",

        @field:SerializedName("image")
        val image: String? = "",

        @field:SerializedName("extendedIngredients")
        val extendedIngredients: List<ExtendedIngredientsItem> = listOf(),

        @field:SerializedName("dishTypes")
        val dishTypes: List<String> = listOf(),

        @field:SerializedName("spoonacularScore")
        val spoonacularScore: Int = 0,

        @field:SerializedName("aggregateLikes")
        val aggregateLikes: Int = 0,

        @field:SerializedName("sourceName")
        val sourceName: String? = "",

        @field:SerializedName("spoonacularSourceUrl")
        val spoonacularSourceUrl: String? = "",
)

data class ExtendedIngredientsItem(
        @field:SerializedName("original")
        val original: String? = "",
)

data class AnalyzedInstructionsItem(

        @field:SerializedName("name")
        val name: String? = "",

        @field:SerializedName("steps")
        val steps: List<StepsItem> = listOf(),
)

data class StepsItem(

        @field:SerializedName("number")
        val number: Int = 0,

        @field:SerializedName("step")
        val step: String? = "",
)