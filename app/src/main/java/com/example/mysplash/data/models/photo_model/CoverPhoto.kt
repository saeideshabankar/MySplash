package com.example.mysplash.data.models.photo_model


import com.example.mysplash.data.models.photo_model.Links
import com.example.mysplash.data.models.photo_model.Urls
import com.example.mysplash.data.models.photo_model.User
import com.google.gson.annotations.SerializedName

data class CoverPhoto(
    @SerializedName("blur_hash")
    val blurHash: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("links")
    val links: Links,
    @SerializedName("urls")
    val urls: Urls,
    @SerializedName("user")
    val user: User,
    @SerializedName("width")
    val width: Int
)