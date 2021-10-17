package com.example.mysplash.ui.main.home_frag

import com.example.mysplash.data.sevises.ApiClient
import com.example.mysplash.ui.base_frag.BasePresenterImpl
import com.example.mysplash.utils.ACCESS_API_KEY
import com.example.mysplash.utils.applyIoScheduler

class HomeApiPresenterImpl constructor( val view: HomeApiContract.View): BasePresenterImpl(),
    HomeApiContract.Presenter {

    override fun callHomeApiPhotoList(client_id: String,sort:String) {
        if (view.checkNetWorkConnection()) {

            view.showLoader()
            disposable = ApiClient.getInstance().apiUseCase()
                .getListOfHomePhotos(ACCESS_API_KEY,sort)
                .applyIoScheduler()
                .subscribe({ response ->
                    view.hideLoader()

                    //error
                    when (response.code()) {
                        in 200..299 -> {
                            response.body()?.let {

                                view.loadHomeApiPhotoList(it)
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