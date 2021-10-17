package com.example.mysplash.ui.main.home_frag

import com.example.mysplash.data.models.photo_model.PhotoModel
import com.example.mysplash.ui.base_frag.BasePresenter
import com.example.mysplash.ui.base_frag.BaseView

interface HomeApiContract {

    interface View : BaseView {
        fun loadHomeApiPhotoList(data: MutableList<PhotoModel>)
    }

    interface Presenter : BasePresenter {
        fun callHomeApiPhotoList(client_id:String,sort:String)
    }
}