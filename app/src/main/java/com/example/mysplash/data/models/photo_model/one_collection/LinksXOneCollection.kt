package com.example.mysplash.data.models.photo_model.one_collection


import com.google.gson.annotations.SerializedName

data class LinksXOneCollection(
    @SerializedName("html")
    val html: String,
    @SerializedName("likes")
    val likes: String,
    @SerializedName("photos")
    val photos: String,
    @SerializedName("portfolio")
    val portfolio: String,
    @SerializedName("self")
    val self: String
)