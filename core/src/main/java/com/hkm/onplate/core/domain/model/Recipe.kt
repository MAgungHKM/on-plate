package com.hkm.onplate.core.domain.model

data class Recipe(
        var recipeId: String = "",
        var title: String = "",
        var summary: String = "",
        var score: String = "",
        var ingredients: List<String> = listOf(),
        var instructions: List<String> = listOf(),
        var readyTime: String = "",
        var servings: String = "",
        var likes: String = "",
        var imageUrl: String = "",
        var dishType: String = "",
        var source: String = "",
        var sourceUrl: String = "",
)