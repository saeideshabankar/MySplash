package com.example.mysplash.ui.base_frag

//is base settings when we communicate with server and we wants to show client directly
interface BaseView {

    //checkNetwork
    fun checkNetWorkConnection(): Boolean

    //errorNetWor
    fun errorNetWorkConnection()

    //show error when network not200
    fun responseCodeError()

    //show error when response failed when internet has error
    fun responseError(error: Throwable)

    // show server error
    fun serverError(error: String)

    //show shimmer
    fun showLoader()
    fun hideLoader()


}