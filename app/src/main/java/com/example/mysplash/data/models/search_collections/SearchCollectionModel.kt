package com.example.mysplash.data.models.search_collections


import com.google.gson.annotations.SerializedName

data class SearchCollectionModel(
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)