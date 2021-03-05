package com.hkm.onplate.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AutoCompleteResponse(

        @field:SerializedName("id")
        val id: Int,

        @field:SerializedName("title")
        val title: String,

        @field:SerializedName("imageType")
        val imageType: String
)
