package com.hkm.onplate.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteRecipeEntity(
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "recipeId")
        var recipeId: String,

        @ColumnInfo(name = "title")
        var title: String,

        @ColumnInfo(name = "summary")
        var summary: String,

        @ColumnInfo(name = "score")
        var score: String,

        @ColumnInfo(name = "ingredients")
        var ingredients: List<String>,

        @ColumnInfo(name = "instructions")
        var instructions: List<String>,

        @ColumnInfo(name = "readyTime")
        var readyTime: String,

        @ColumnInfo(name = "servings")
        var servings: String,

        @ColumnInfo(name = "likes")
        var likes: String,

        @ColumnInfo(name = "imageUrl")
        var imageUrl: String,

        @ColumnInfo(name = "dishType")
        var dishType: String,

        @ColumnInfo(name = "source")
        var source: String = "",

        @ColumnInfo(name = "sourceUrl")
        var sourceUrl: String = "",
)
