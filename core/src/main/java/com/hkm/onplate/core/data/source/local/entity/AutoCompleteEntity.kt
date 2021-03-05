package com.hkm.onplate.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "autocomplete")
class AutoCompleteEntity(
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "autocompleteId")
        val autocompleteId: String,

        @ColumnInfo(name = "title")
        val title: String,

        @ColumnInfo(name = "imageType")
        val imageType: String,

        @ColumnInfo(name = "imageUrl")
        val imageUrl: String
)