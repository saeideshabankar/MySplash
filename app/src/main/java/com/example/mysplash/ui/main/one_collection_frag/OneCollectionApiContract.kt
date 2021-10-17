package com.example.mysplash.ui.main.one_collection_frag

import com.example.mysplash.data.models.photo_model.one_collection.OneCollectionModel
import com.example.mysplash.ui.base_frag.BasePresenter
import com.example.mysplash.ui.base_frag.BaseView

interface OneCollectionApiContract {

    interface View : BaseView {
        fun loadOneCollectionApiPhotoList(data: MutableList<OneCollectionModel>)
    }

    interface Presenter : BasePresenter {
        fun callOneCollectionApiPhotoList(photoId:String,client_id:String)
    }
}