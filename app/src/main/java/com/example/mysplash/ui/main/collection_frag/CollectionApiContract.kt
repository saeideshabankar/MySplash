package com.example.mysplash.ui.main.collection_frag

import com.example.mysplash.data.models.photo_model.CollectionModel
import com.example.mysplash.ui.base_frag.BasePresenter
import com.example.mysplash.ui.base_frag.BaseView

interface CollectionApiContract {

    interface View : BaseView {
        fun loadCollectionsApi(data: MutableList<CollectionModel>)
    }

    interface Presenter : BasePresenter {

        fun callCollectionsApi(client_id:String)
    }
}