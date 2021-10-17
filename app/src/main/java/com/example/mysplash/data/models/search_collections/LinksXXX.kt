package com.example.mysplash.data.models.search_collections


import com.google.gson.annotations.SerializedName

data class LinksXXX(
    @SerializedName("html")
    val html: String,
    @SerializedName("likes")
    val likes: String,
    @SerializedName("photos")
    val photos: String,
    @SerializedName("self")
    val self: String
)