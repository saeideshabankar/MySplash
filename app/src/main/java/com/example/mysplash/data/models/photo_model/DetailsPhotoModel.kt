package com.example.mysplash.data.models.photo_model


import com.google.gson.annotations.SerializedName

data class DetailsPhotoModel(
    @SerializedName("blur_hash")
    val blurHash: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("current_user_collections")
    val currentUserCollections: MutableList<CurrentUserCollection>,
    @SerializedName("description")
    val description: String,
    @SerializedName("downloads")
    val downloads: Int,
    @SerializedName("exif")
    val exif: Exif,
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("links")
    val links: LinksXXXX,
    @SerializedName("location")
    val location: Location,
    @SerializedName("tags")
    val tags: MutableList<Tag>,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("urls")
    val urls: Urls,
    @SerializedName("user")
    val user: UserXX,
    @SerializedName("width")
    val width: Int
)