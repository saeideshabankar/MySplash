package com.example.mysplash.data.sevises

import com.example.mysplash.data.models.photo_model.CollectionModel
import com.example.mysplash.data.models.photo_model.DetailsPhotoModel
import com.example.mysplash.data.models.photo_model.PhotoModel
import com.example.mysplash.data.models.photo_model.one_collection.OneCollectionModel
import com.example.mysplash.data.models.search_collections.SearchCollectionModel
import com.example.mysplash.data.models.search_photo.SearchPhotoModel
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    //get list of photo
    @GET("photos")
    fun listOfPhotoApi(
        @Query("client_id") clientId: String,
        @Query("order_by") sort: String
    ): Single<Response<MutableList<PhotoModel>>>

     @GET("collections")
    fun listOfCollectionApi(
        @Query("client_id") clientId: String
    ): Single<Response<MutableList<CollectionModel>>>

  @GET("collections/{id}/photos")
    fun listOfOneCollectionApi(
      @Path("id") id: String,
        @Query("client_id") clientId: String
    ): Single<Response<MutableList<OneCollectionModel>>>


    @GET("photos/{id}")
    fun detailsPhotoListApi(
        @Path("id") photoId: String,
         @Query("client_id") clientId: String
    ): Single<Response<DetailsPhotoModel>>

    @GET("search/photos")
    fun listOfSearchPhotosApi(
        @Query("query") searchPhoto: String,
        @Query("client_id") clientId: String
    ): Single<Response<SearchPhotoModel>>
@GET("search/collections")
    fun listOfSearchCollectionsApi(
        @Query("query") searchCollections: String,
        @Query("client_id") clientId: String
    ): Single<Response<SearchCollectionModel>>


}