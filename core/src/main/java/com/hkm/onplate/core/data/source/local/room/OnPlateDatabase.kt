package com.hkm.onplate.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hkm.onplate.core.data.source.local.converter.StringListConverter
import com.hkm.onplate.core.data.source.local.entity.AutoCompleteEntity
import com.hkm.onplate.core.data.source.local.entity.FavoriteRecipeEntity
import com.hkm.onplate.core.data.source.local.entity.RecipeEntity

@Database(entities = [RecipeEntity::class, FavoriteRecipeEntity::class, AutoCompleteEntity::class], version = 4, exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class OnPlateDatabase : RoomDatabase() {

    abstract fun onPlateDao(): OnPlateDao
}