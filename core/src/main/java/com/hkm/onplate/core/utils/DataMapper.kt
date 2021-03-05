package com.hkm.onplate.core.utils

import android.util.Log
import com.hkm.onplate.core.data.source.local.entity.AutoCompleteEntity
import com.hkm.onplate.core.data.source.local.entity.FavoriteRecipeEntity
import com.hkm.onplate.core.data.source.local.entity.RecipeEntity
import com.hkm.onplate.core.data.source.remote.response.AutoCompleteResponse
import com.hkm.onplate.core.data.source.remote.response.RecipeResponse
import com.hkm.onplate.core.domain.model.Recipe
import org.jsoup.Jsoup
import com.hkm.onplate.core.presentation.model.Recipe as PresentationRecipe

object DataMapper {
    fun mapRecipeResponsesToItsEntities(input: List<RecipeResponse?>): List<RecipeEntity> {
        val recipeList = ArrayList<RecipeEntity>()
        input.map {
            if (it != null) {
                val listIngredients = ArrayList<String>()
                val listInstructions = ArrayList<String>()
                val dishType = it.dishTypes.joinToString(", ")

                for (ingredient in it.extendedIngredients) {
                    listIngredients.add(ingredient.original.toString())
                }

                for (instructions in it.analyzedInstructions) {
                    for (instruction in instructions.steps) {
                        listInstructions.add("${instruction.number}. ${instruction.step}")
                    }
                }

                val summary = Jsoup.parseBodyFragment(it.summary).body().text()

                val recipe = RecipeEntity(
                        recipeId = it.id.toString(),
                        summary = summary,
                        title = it.title.toString(),
                        score = "${it.spoonacularScore}%",
                        ingredients = listIngredients.toList(),
                        instructions = listInstructions.toList(),
                        readyTime = "Ready in ${it.readyInMinutes} minutes",
                        servings = "${it.servings} servings",
                        likes = it.aggregateLikes.toString(),
                        imageUrl = it.image.toString(),
                        dishType = dishType,
                        source = it.sourceName.toString(),
                        sourceUrl = it.spoonacularSourceUrl.toString()
                )
                recipeList.add(recipe)
            }
        }
        return recipeList
    }

    fun mapRecipeResponseToItsEntity(input: RecipeResponse?): RecipeEntity =
            if (input != null) {
                Log.d("here", "yeh here")
                val listIngredients = ArrayList<String>()
                val listInstructions = ArrayList<String>()
                val dishType = input.dishTypes.joinToString(", ")

                for (ingredient in input.extendedIngredients) {
                    listIngredients.add(ingredient.original.toString())
                }

                for (instructions in input.analyzedInstructions) {
                    for (instruction in instructions.steps) {
                        listInstructions.add("${instruction.number}. ${instruction.step}")
                    }
                }

                val summary = Jsoup.parseBodyFragment(input.summary).body().text()

                RecipeEntity(
                        recipeId = input.id.toString(),
                        summary = summary,
                        title = input.title.toString(),
                        score = "${input.spoonacularScore}%",
                        ingredients = listIngredients.toList(),
                        instructions = listInstructions.toList(),
                        readyTime = "Ready in ${input.readyInMinutes} minutes",
                        servings = "${input.servings} servings",
                        likes = input.aggregateLikes.toString(),
                        imageUrl = input.image.toString(),
                        dishType = dishType,
                        source = input.sourceName.toString(),
                        sourceUrl = input.spoonacularSourceUrl.toString()
                )
            } else RecipeEntity()

    fun mapAutoCompleteResponseToItsEntities(input: List<AutoCompleteResponse?>): List<AutoCompleteEntity> {
        val resultList = ArrayList<AutoCompleteEntity>()
        input.map {
            if (it != null) {
                val imageUrl = "https://spoonacular.com/recipeImages/${it.id}-90x90.${it.imageType}"

                val result = AutoCompleteEntity(
                        autocompleteId = it.id.toString(),
                        title = it.title,
                        imageType = it.imageType,
                        imageUrl = imageUrl,
                )
                resultList.add(result)
            }
        }
        return resultList
    }

    fun mapRecipeEntitiesToItsDomain(input: List<RecipeEntity>): List<Recipe> =
            input.map {
                Recipe(
                        recipeId = it.recipeId,
                        summary = it.summary,
                        title = it.title,
                        score = it.score,
                        ingredients = it.ingredients,
                        instructions = it.instructions,
                        readyTime = it.readyTime,
                        servings = it.servings,
                        likes = it.likes,
                        imageUrl = it.imageUrl,
                        dishType = it.dishType,
                        source = it.source,
                        sourceUrl = it.sourceUrl
                )
            }

    fun mapAutoCompleteEntitiesToItsRecipeEntity(input: List<AutoCompleteEntity>): List<RecipeEntity> =
            input.map {
                RecipeEntity(
                        recipeId = it.autocompleteId,
                        title = it.title,
                        score = "",
                        summary = "",
                        ingredients = listOf(),
                        instructions = listOf(),
                        readyTime = "",
                        servings = "",
                        likes = "",
                        imageUrl = it.imageUrl,
                        dishType = "",
                        source = "",
                        sourceUrl = "",
                )
            }

    fun mapRecipeDomainsToItsPresentation(input: List<Recipe>): List<PresentationRecipe> =
            input.map {
                PresentationRecipe(
                        recipeId = it.recipeId,
                        summary = it.summary,
                        title = it.title,
                        score = it.score,
                        ingredients = it.ingredients,
                        instructions = it.instructions,
                        readyTime = it.readyTime,
                        servings = it.servings,
                        likes = it.likes,
                        imageUrl = it.imageUrl,
                        dishType = it.dishType,
                        source = it.source,
                        sourceUrl = it.sourceUrl
                )
            }

    fun mapRecipeEntityToFavoriteRecipeEntity(input: RecipeEntity): FavoriteRecipeEntity =
            FavoriteRecipeEntity(
                    recipeId = input.recipeId,
                    summary = input.summary,
                    title = input.title,
                    score = input.score,
                    ingredients = input.ingredients,
                    instructions = input.instructions,
                    readyTime = input.readyTime,
                    servings = input.servings,
                    likes = input.likes,
                    imageUrl = input.imageUrl,
                    dishType = input.dishType,
                    source = input.source,
                    sourceUrl = input.sourceUrl
            )


    fun mapFavoriteRecipeEntitiesToRecipeEntity(input: List<FavoriteRecipeEntity>): List<RecipeEntity> =
            input.map {
                RecipeEntity(
                        recipeId = it.recipeId,
                        summary = it.summary,
                        title = it.title,
                        score = it.score,
                        ingredients = it.ingredients,
                        instructions = it.instructions,
                        readyTime = it.readyTime,
                        servings = it.servings,
                        likes = it.likes,
                        imageUrl = it.imageUrl,
                        dishType = it.dishType,
                        source = it.source,
                        sourceUrl = it.sourceUrl
                )
            }

    fun mapFavoriteRecipeEntityToRecipeEntity(input: FavoriteRecipeEntity): RecipeEntity =
            RecipeEntity(
                    recipeId = input.recipeId,
                    summary = input.summary,
                    title = input.title,
                    score = input.score,
                    ingredients = input.ingredients,
                    instructions = input.instructions,
                    readyTime = input.readyTime,
                    servings = input.servings,
                    likes = input.likes,
                    imageUrl = input.imageUrl,
                    dishType = input.dishType,
                    source = input.source,
                    sourceUrl = input.sourceUrl
            )

    fun mapRecipeEntityToItsDomain(input: RecipeEntity): Recipe =
            Recipe(
                    recipeId = input.recipeId,
                    summary = input.summary,
                    title = input.title,
                    score = input.score,
                    ingredients = input.ingredients,
                    instructions = input.instructions,
                    readyTime = input.readyTime,
                    servings = input.servings,
                    likes = input.likes,
                    imageUrl = input.imageUrl,
                    dishType = input.dishType,
                    source = input.source,
                    sourceUrl = input.sourceUrl
            )

    fun mapRecipeDomainToItsPresentation(input: Recipe): PresentationRecipe =
            PresentationRecipe(
                    recipeId = input.recipeId,
                    summary = input.summary,
                    title = input.title,
                    score = input.score,
                    ingredients = input.ingredients,
                    instructions = input.instructions,
                    readyTime = input.readyTime,
                    servings = input.servings,
                    likes = input.likes,
                    imageUrl = input.imageUrl,
                    dishType = input.dishType,
                    source = input.source,
                    sourceUrl = input.sourceUrl
            )

    fun mapRecipePresentationToItsDomain(input: PresentationRecipe): Recipe =
            Recipe(
                    recipeId = input.recipeId,
                    summary = input.summary,
                    title = input.title,
                    score = input.score,
                    ingredients = input.ingredients,
                    instructions = input.instructions,
                    readyTime = input.readyTime,
                    servings = input.servings,
                    likes = input.likes,
                    imageUrl = input.imageUrl,
                    dishType = input.dishType,
                    source = input.source,
                    sourceUrl = input.sourceUrl
            )

    fun mapRecipeDomainToItsEntity(input: Recipe) = RecipeEntity(
            recipeId = input.recipeId,
            summary = input.summary,
            title = input.title,
            score = input.score,
            ingredients = input.ingredients,
            instructions = input.instructions,
            readyTime = input.readyTime,
            servings = input.servings,
            likes = input.likes,
            imageUrl = input.imageUrl,
            dishType = input.dishType,
            source = input.source,
            sourceUrl = input.sourceUrl
    )
}