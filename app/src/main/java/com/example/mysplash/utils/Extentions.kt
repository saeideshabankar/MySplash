package com.example.mysplash.utils

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.DecimalFormat

//custom scheduler for Rx
fun <T> Single<T>.applyScheduler(scheduler: Scheduler) =
    subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.applyIoScheduler() = applyScheduler(Schedulers.io())

//RecyclerView
fun RecyclerView.initRecyclerViewWithApply(
    layoutManager: RecyclerView.LayoutManager,
    adapter: RecyclerView.Adapter<*>
) {
    this.apply {
        this.layoutManager = layoutManager
        this.setHasFixedSize(true)
        this.fitsSystemWindows = true
        this.itemAnimator = DefaultItemAnimator()
        this.adapter = adapter
    }

}

//check network available
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    val activeNet = activeNetworkInfo != null && activeNetworkInfo.isConnected
    return if (activeNet) {
        true
    } else {
        Toast.makeText(this, "Please turn on your internet connection ", Toast.LENGTH_SHORT).show()
        false
    }
}

fun isEmptyString(text: String?): Boolean {
    return text == null || text.trim { it <= ' ' } == "null" || text.trim { it <= ' ' }
        .isEmpty() || TextUtils.isEmpty(
        text
    )
}

//Intent
fun Context.intentForEveryWhere(activity: Class<*>) {
    Intent(this, activity).apply {
        startActivity(this)
    }
}

//Money separating
fun Any.moneySeparating(): String {
    return DecimalFormat(",###.##").format(this).plus("$")
}

fun ImageView.showImageGlide(context: Context, url: String, loader: ProgressBar) {
    Glide.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        //  .placeholder(ContextCompat.getDrawable(context, R.drawable.person_24))
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                loader.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                loader.visibility = View.GONE
                return false
            }
        })
        .into(this)
}