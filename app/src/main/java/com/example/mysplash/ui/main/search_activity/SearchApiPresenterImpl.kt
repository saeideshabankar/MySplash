package com.example.mysplash.ui.main.search_activity

import com.example.mysplash.data.sevises.ApiClient
import com.example.mysplash.ui.base_frag.BasePresenterImpl
import com.example.mysplash.utils.ACCESS_API_KEY
import com.example.mysplash.utils.applyIoScheduler

class SearchApiPresenterImpl constructor(val view: SearchApiContract.View): BasePresenterImpl(),
    SearchApiContract.Presenter {

    override fun callSearchPhotoApiList(searchPhoto: String, client_id: String) {

        if (view.checkNetWorkConnection()) {

            view.showLoader()
            disposable = ApiClient.getInstance().apiUseCase()
                .getSearchApiPhotoList(searchPhoto,ACCESS_API_KEY)
                .applyIoScheduler()
                .subscribe({ response ->
                    view.hideLoader()

                    //error
                    when (response.code()) {
                        in 200..299 -> {
                            response.body()?.let {

                                view.loadSearchPhotoApiPhotoList(it)
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

    override fun callSearchCollectionsApiList(searchCollection: String, client_id: String) {
        if (view.checkNetWorkConnection()) {

            view.showLoader()
            disposable = ApiClient.getInstance().apiUseCase()
                .getSearchCollectionsApiList(searchCollection ,ACCESS_API_KEY)
                .applyIoScheduler()
                .subscribe({ response ->
                    view.hideLoader()

                    //error
                    when (response.code()) {
                        in 200..299 -> {
                            response.body()?.let {

                                view.loadSearchCollectionsApiList(it)
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
}