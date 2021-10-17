package com.example.mysplash.ui.base_frag

import androidx.annotation.NonNull
import io.reactivex.disposables.Disposable

open class BasePresenterImpl : BasePresenter {
    //hame connection internet haro mirizim toye disposable
    //agar disposable por shod dg por shode va bia ertebat ro ghat kon bad az inke gereft alaki ertebat bar gharar nabashe
    @NonNull
    var disposable: Disposable? = null
    override fun onStop() {
        disposable?.let {
            it.dispose()
        }
    }
}