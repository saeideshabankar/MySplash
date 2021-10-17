package com.example.mysplash.ui.main.collection_frag.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysplash.R
import com.example.mysplash.data.models.photo_model.CollectionModel
import com.example.mysplash.utils.COLLECTION_FRAG
import com.example.mysplash.utils.EventSendId
import com.example.mysplash.utils.ONE_COLLECTION_FRAG
import com.example.mysplash.utils.showImageGlide
import org.greenrobot.eventbus.EventBus

class RecyclerCollectionAdapter(
    private val context: Context,
    private val collectionListModel: MutableList<CollectionModel>
) :
    RecyclerView.Adapter<RecyclerCollectionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.collection_rv_item_adapter, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val id=collectionListModel[position].id
        val username1=collectionListModel[position].coverPhoto.user.username
        val totalPhoto1=collectionListModel[position].coverPhoto.user.totalPhotos
        val collectionTitle1=collectionListModel[position].title
        val bigImg1=collectionListModel[position].coverPhoto.urls.thumb
        val profileImg1=collectionListModel[position].user.profileImage.medium
        holder.bindData(id,username1,bigImg1,profileImg1,totalPhoto1,collectionTitle1)
    }

    override fun getItemCount(): Int {
        return collectionListModel.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val userNameTv = itemView.findViewById(R.id.collection_username_tv) as TextView
        private val titleCollection = itemView.findViewById(R.id.collection_picLocation) as TextView
        private val numberOfPhoto = itemView.findViewById(R.id.collection_numberOfPhoto) as TextView

          private val profileItemImg1 = itemView.findViewById(R.id.collection_profile_item_img) as ImageView
        private val bigItemImg = itemView.findViewById(R.id.collection_big_item_img) as ImageView

        private val progressItem = itemView.findViewById(R.id.collection_progress_item) as ProgressBar
        fun bindData(
            id:String,
            username1: String,
            bigImg1: String,
            profileImg1: String,
            totalPhoto1: Int,
            collectionTitle1: String
        ) {
                    titleCollection.setTextColor(Color.WHITE)
                    numberOfPhoto.setTextColor(Color.WHITE)




            numberOfPhoto.visibility = View.VISIBLE
            titleCollection.visibility = View.VISIBLE
            userNameTv.text = username1
            titleCollection.text = collectionTitle1
            numberOfPhoto.text = totalPhoto1.toString()
           // profileItemImg.showImageGlide(context, profileImg1, progressItem)
            Glide.with(context)
                .load(profileImg1)
                .into(profileItemImg1)
            bigItemImg.showImageGlide(context, bigImg1, progressItem)
            itemView.setOnClickListener {
                //run in collection frag
                EventBus.getDefault().post(EventSendId.OnSendIdToOneCollection(id,collectionTitle1,COLLECTION_FRAG,
                    ONE_COLLECTION_FRAG))

            }

        }
    }
}