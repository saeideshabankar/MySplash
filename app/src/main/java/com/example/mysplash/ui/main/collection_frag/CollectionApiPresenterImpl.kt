package com.example.mysplash.ui.main.collection_frag

import com.example.mysplash.data.sevises.ApiClient
import com.example.mysplash.ui.base_frag.BasePresenterImpl
import com.example.mysplash.utils.ACCESS_API_KEY
import com.example.mysplash.utils.applyIoScheduler

class CollectionApiPresenterImpl constructor(val view: CollectionApiContract.View): BasePresenterImpl(),
    CollectionApiContract.Presenter {

    override fun callCollectionsApi(client_id: String) {

        if (view.checkNetWorkConnection()) {

            view.showLoader()
            disposable = ApiClient.getInstance().apiUseCase()
                .getListOfCollections(ACCESS_API_KEY)
                .applyIoScheduler()
                .subscribe({ response ->
                    view.hideLoader()

                    when (response.code()) {
                         200 -> {

                            response.body()?.let {


                                view.loadCollectionsApi(it)
                            }
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