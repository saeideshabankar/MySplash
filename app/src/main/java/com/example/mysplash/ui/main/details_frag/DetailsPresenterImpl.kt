package com.example.mysplash.ui.main.details_frag

import com.example.mysplash.data.sevises.ApiClient
import com.example.mysplash.ui.base_frag.BasePresenterImpl
import com.example.mysplash.utils.applyIoScheduler

class DetailsPresenterImpl constructor(val view: DetailsContract.View) : BasePresenterImpl(),
    DetailsContract.Presenter {
    override fun callDetailsApiList(id: String, api_key: String) {
        if (view.checkNetWorkConnection()) {
            view.showLoader()
            disposable = ApiClient.getInstance().apiUseCase()
                .getDetailsPhotoList(id, api_key)
                .applyIoScheduler()
                .subscribe({ response ->
                    view.hideLoader()
                    //error
                    when (response.code()) {
                        in 200..299 -> {
                            response.body()?.let {
                                view.loadDetailsApiList(it)
                            }
                        }
                        201 -> {
                            // view.gotoLoginPage()
                        }
                         400 -> {

                            view.serverError("Bad Request,The request was unacceptable, often due to missing a required parameter")
                        }
                        401 -> {
                            view.serverError("Unauthorized ,Invalid Access Token")
                        }
                        403 -> {
                            view.serverError("Forbidden ,Missing permissions to perform request")
                        }
                        404 -> {
                            view.serverError( "Not Found, The requested resource doesn’t exist")
                        }
                        500, 503-> {
                            view.serverError(	"Something went wrong on our end" )
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

    /*override fun callImagesListApiList(movieId: Int, api_key: String) {
        if (view.checkNetWorkConnection()) {

            view.showLoader()
            disposable = ApiClient.getInstance().apiUseCase()
                .getImagesList(movieId,api_key)
                .applyIoScheduler()
                .subscribe({ response ->
                    view.hideLoader()

                    //error
                    when (response.code()) {
                        in 200..299 -> {
                            response.body()?.let {

                                Log.i("salam", "callImagesListApiList: ${it.id}")
                                view.loadImagesApiList(it.posters)
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
    }*/

}
