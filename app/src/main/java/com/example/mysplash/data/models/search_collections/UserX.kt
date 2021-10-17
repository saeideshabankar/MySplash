package com.example.mysplash.data.models.search_collections


import com.google.gson.annotations.SerializedName

data class UserX(
    @SerializedName("bio")
    val bio: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("links")
    val links: LinksXXX,
    @SerializedName("name")
    val name: String,
    @SerializedName("portfolio_url")
    val portfolioUrl: Any,
    @SerializedName("profile_image")
    val profileImage: ProfileImageX,
    @SerializedName("username")
    val username: String
)