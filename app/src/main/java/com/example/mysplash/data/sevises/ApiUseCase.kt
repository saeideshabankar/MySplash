package com.example.mysplash.data.sevises

import com.example.mysplash.data.models.photo_model.CollectionModel
import com.example.mysplash.data.models.photo_model.DetailsPhotoModel
import com.example.mysplash.data.models.photo_model.PhotoModel
import com.example.mysplash.data.models.photo_model.one_collection.OneCollectionModel
import com.example.mysplash.data.models.search_collections.SearchCollectionModel
import com.example.mysplash.data.models.search_photo.SearchPhotoModel
import io.reactivex.Single
import retrofit2.Response


open class ApiUseCase constructor(private val apiServices: ApiServices) {

    fun getListOfHomePhotos(client_id: String,sort:String): Single<Response<MutableList<PhotoModel>>> {
        return apiServices.listOfPhotoApi(client_id,sort)
    }
    fun getListOfCollections(client_id: String): Single<Response<MutableList<CollectionModel>>> {
        return apiServices.listOfCollectionApi(client_id)
    } fun getListOfOneCollections(photoId: String,client_id: String): Single<Response<MutableList<OneCollectionModel>>> {
        return apiServices.listOfOneCollectionApi(photoId,client_id)
    }
    fun getDetailsPhotoList(photoId: String, client_id: String): Single<Response<DetailsPhotoModel>> {
        return apiServices.detailsPhotoListApi(photoId, client_id)
    }
    fun getSearchApiPhotoList(searchPhoto: String, client_id: String): Single<Response<SearchPhotoModel>> {
        return apiServices.listOfSearchPhotosApi(searchPhoto, client_id)
    }
    fun getSearchCollectionsApiList(collectionPhoto: String, client_id: String): Single<Response<SearchCollectionModel>> {
        return apiServices.listOfSearchCollectionsApi(collectionPhoto, client_id)
    }
}