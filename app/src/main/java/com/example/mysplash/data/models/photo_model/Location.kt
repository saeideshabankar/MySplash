package com.example.mysplash.data.models.photo_model


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("position")
    val position: Position
)