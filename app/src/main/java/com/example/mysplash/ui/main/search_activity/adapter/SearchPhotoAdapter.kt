package com.example.mysplash.ui.main.search_activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysplash.R
import com.example.mysplash.data.models.search_photo.Result
import com.example.mysplash.utils.EventSendId
import com.example.mysplash.utils.SEARCH_PHOTO_FRAG
import com.example.mysplash.utils.showImageGlide
import org.greenrobot.eventbus.EventBus

class SearchPhotoAdapter constructor(
    private val context: Context,
    private val photoListModel: MutableList<Result>
) :
    RecyclerView.Adapter<SearchPhotoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.home_rv_item_adapter, parent, false))
    }

    override fun onBindViewHolder(holder: SearchPhotoAdapter.ViewHolder, position: Int) {
        var username = photoListModel[position].user.username
        // var location=photoListModel[position].user.location
        var bigImg = photoListModel[position].urls.thumb
        var profileImg = photoListModel[position].user.profileImage.medium
        val id = photoListModel[position].id

        holder.bindData(id, username, bigImg, profileImg)
    }

    override fun getItemCount(): Int {
        return photoListModel.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val userNameTv = itemView.findViewById(R.id.username_tv) as TextView
        private val titleCollection = itemView.findViewById(R.id.picLocation) as TextView
        private val numberOfPhoto = itemView.findViewById(R.id.numberOfPhoto) as TextView

        private val profileItemImg = itemView.findViewById(R.id.profile_item_img) as ImageView
        private val bigItemImg = itemView.findViewById(R.id.big_item_img) as ImageView

        private val progressItem = itemView.findViewById(R.id.progress_item) as ProgressBar
        fun bindData(id: String, username: String, bigImg: String, profileImg: String) {

            numberOfPhoto.visibility = View.GONE
            titleCollection.visibility = View.GONE
            userNameTv.text = username
           // profileItemImg.showImageGlide(context, profileImg, progressItem)
            Glide.with(context)
                .load(profileImg)
                .into(profileItemImg)

            bigItemImg.showImageGlide(context, bigImg, progressItem)

            itemView.setOnClickListener {
               EventBus.getDefault().post(EventSendId.OnSendSearchIdToDetails(id,profileImg,
                   SEARCH_PHOTO_FRAG,""))
            }

        }
    }
}