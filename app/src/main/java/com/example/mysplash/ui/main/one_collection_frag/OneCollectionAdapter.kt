package com.example.mysplash.ui.main.one_collection_frag

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mysplash.R
import com.example.mysplash.data.models.photo_model.one_collection.OneCollectionModel
import com.example.mysplash.utils.EventSendId
import com.example.mysplash.utils.FROM_SEARCH_TO_ONE_COLLECTION_FRAG
import com.example.mysplash.utils.ONE_COLLECTION_FRAG
import com.example.mysplash.utils.showImageGlide
import org.greenrobot.eventbus.EventBus

class OneCollectionAdapter(
    private val context: Context,
    private val pageFlag: String,
    private val OneCollectionPhotoListModel: MutableList<OneCollectionModel>
) :
    RecyclerView.Adapter<OneCollectionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.home_rv_item_adapter, parent, false))
    }

    override fun onBindViewHolder(holder: OneCollectionAdapter.ViewHolder, position: Int) {
        val username = OneCollectionPhotoListModel[position].userOneCollection.username
        // var location=photoListModel[position].user.location
        val bigImg = OneCollectionPhotoListModel[position].urls.thumb
        val profileImg = OneCollectionPhotoListModel[position].userOneCollection.profileImage.medium
        val id = OneCollectionPhotoListModel[position].id

        holder.bindData(id, username, bigImg, profileImg)
    }

    override fun getItemCount(): Int {
        return OneCollectionPhotoListModel.size
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
            profileItemImg.showImageGlide(context, profileImg, progressItem)
            bigItemImg.showImageGlide(context, bigImg, progressItem)

            itemView.setOnClickListener {
              //  Toast.makeText(context, pageFlag, Toast.LENGTH_SHORT).show()
//                ON SEND ID TO DETAILS WRITE IN HOME FRAG
                if (pageFlag == ONE_COLLECTION_FRAG) {
                    EventBus.getDefault()
                        .post(EventSendId.OnSendIdToDetails(id, profileImg, pageFlag))

                } else if (pageFlag == FROM_SEARCH_TO_ONE_COLLECTION_FRAG) {

//                ON SEND ID TO DETAILS WRITE IN HOME FRAG
                    EventBus.getDefault()
                        .post(EventSendId.OnSendSearchIdToDetails(id, profileImg, pageFlag, ""))

                }
            }
        }
    }
}