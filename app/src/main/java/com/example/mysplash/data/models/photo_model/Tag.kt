package com.example.mysplash.data.models.photo_model


import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("title")
    val title: String
)