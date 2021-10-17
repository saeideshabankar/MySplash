package com.example.mysplash.data.models.search_photo


import com.google.gson.annotations.SerializedName

data class SearchPhotoModel(
    @SerializedName("results")
    val results: MutableList<Result>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)