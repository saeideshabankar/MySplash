package com.example.mysplash.ui.main.one_collection_frag

import com.example.mysplash.data.sevises.ApiClient
import com.example.mysplash.ui.base_frag.BasePresenterImpl
import com.example.mysplash.utils.ACCESS_API_KEY
import com.example.mysplash.utils.applyIoScheduler

class OneCollectionApiPresenterImpl constructor(val view: OneCollectionApiContract.View): BasePresenterImpl(),
    OneCollectionApiContract.Presenter {

    override fun callOneCollectionApiPhotoList(photoId: String, client_id: String) {

        if (view.checkNetWorkConnection()) {

            view.showLoader()
            disposable = ApiClient.getInstance().apiUseCase()
                .getListOfOneCollections(photoId,ACCESS_API_KEY)
                .applyIoScheduler()
                .subscribe({ response ->
                    view.hideLoader()

                    //error
                    when (response.code()) {
                        in 200..299 -> {
                            response.body()?.let {


                                view.loadOneCollectionApiPhotoList(it)
                            }

                        }
                        201 -> {
                            // view.gotoLoginPage()
                        }
                        in 400..499 -> {
                            view.serverError("Error")
                        }
                        422 -> {
                            view.serverError("validation error")
                        }
                        404 -> {
                            view.serverError("Not found")
                        }
                        in 500..599 -> {

                        }
                        503 -> {

                        }
                    }
                }, { error ->
                    view.hideLoader()
                    view.responseError(error)
                })
        } else {
            view.errorNetWorkConnection()
        }
    }
}