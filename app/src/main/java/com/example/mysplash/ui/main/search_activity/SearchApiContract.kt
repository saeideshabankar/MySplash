package com.example.mysplash.ui.main.search_activity

import com.example.mysplash.data.models.search_collections.SearchCollectionModel
import com.example.mysplash.data.models.search_photo.SearchPhotoModel
import com.example.mysplash.ui.base_frag.BasePresenter
import com.example.mysplash.ui.base_frag.BaseView

interface SearchApiContract {

    interface View : BaseView {
        fun loadSearchPhotoApiPhotoList(data: SearchPhotoModel)
        fun loadSearchCollectionsApiList(data: SearchCollectionModel)
    }

    interface Presenter : BasePresenter {
        fun callSearchPhotoApiList(searchPhoto:String,client_id:String)
        fun callSearchCollectionsApiList(searchCollection:String,client_id:String)
    }
}