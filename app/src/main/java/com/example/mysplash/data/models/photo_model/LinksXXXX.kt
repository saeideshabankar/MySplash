package com.example.mysplash.data.models.photo_model


import com.google.gson.annotations.SerializedName

data class LinksXXXX(
    @SerializedName("download")
    val download: String,
    @SerializedName("download_location")
    val downloadLocation: String,
    @SerializedName("html")
    val html: String,
    @SerializedName("self")
    val self: String
)