package com.example.mysplash.ui.main.search_activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mysplash.R
import com.example.mysplash.data.models.search_collections.Result
import com.example.mysplash.utils.EventSendId
import com.example.mysplash.utils.FROM_SEARCH_TO_ONE_COLLECTION_FRAG
import com.example.mysplash.utils.SEARCH_COLLECTION_FRAG
import com.example.mysplash.utils.showImageGlide
import org.greenrobot.eventbus.EventBus

class SearchCollectionsAdapter constructor(
    private val context: Context,
    private val photoListModel: MutableList<Result>
) :
    RecyclerView.Adapter<SearchCollectionsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.home_rv_item_adapter, parent, false))
    }

    override fun onBindViewHolder(holder: SearchCollectionsAdapter.ViewHolder, position: Int) {
        val username = photoListModel[position].user.username
        val title=photoListModel[position].title
        val bigImg = photoListModel[position].coverPhoto.urls.thumb
        val profileImg = photoListModel[position].user.profileImage.medium
        val id = photoListModel[position].id

        holder.bindData(id, username, bigImg, profileImg,title)
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
        private val constOnCover = itemView.findViewById(R.id.constOnCover) as ConstraintLayout
        private val progressItem = itemView.findViewById(R.id.progress_item) as ProgressBar

      fun bindData(id: String, username: String, bigImg: String, profileImg: String,title:String) {

            constOnCover.visibility = View.VISIBLE
            numberOfPhoto.visibility = View.VISIBLE
            titleCollection.visibility = View.VISIBLE
            userNameTv.text = username

            titleCollection.text = title
            profileItemImg.showImageGlide(context, profileImg, progressItem)
            bigItemImg.showImageGlide(context, bigImg, progressItem)

            itemView.setOnClickListener {

                EventBus.getDefault().post(EventSendId.OnSendIdToOneCollection(id,title,
                    SEARCH_COLLECTION_FRAG, FROM_SEARCH_TO_ONE_COLLECTION_FRAG))

            }
        }
    }
}