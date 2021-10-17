package com.example.mysplash.ui.main.details_frag

import com.example.mysplash.data.models.photo_model.DetailsPhotoModel
import com.example.mysplash.ui.base_frag.BasePresenter
import com.example.mysplash.ui.base_frag.BaseView

interface DetailsContract {
    interface View : BaseView {
        fun loadDetailsApiList(data: DetailsPhotoModel)

    }

    interface Presenter : BasePresenter {
        fun callDetailsApiList(id: String, api_key: String)
    }
}