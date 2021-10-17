package com.example.mysplash.ui.main.search_activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysplash.R
import com.example.mysplash.data.models.search_collections.SearchCollectionModel
import com.example.mysplash.data.models.search_photo.Result
import com.example.mysplash.data.models.search_photo.SearchPhotoModel
import com.example.mysplash.ui.main.details_frag.DetailsFragment
import com.example.mysplash.ui.main.search_activity.adapter.SearchPhotoAdapter
import com.example.mysplash.utils.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_collection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class
SearchPhotoFragment : Fragment(), SearchApiContract.View {

    private var searchTxt = ""
    private var id = ""
    private var profileUrl = ""
    private var pageFlag = ""
    private val searchPhotoListModels: MutableList<Result> = mutableListOf()

    private val presenter: SearchApiPresenterImpl by lazy { SearchApiPresenterImpl(this) }
    private val recyclerSearchPhotoAdapter: SearchPhotoAdapter by lazy {
        SearchPhotoAdapter(requireContext(), searchPhotoListModels)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*   val param = main_collections_rv.layoutParams as ViewGroup.MarginLayoutParams
           param.setMargins(0,10,0,0)
           main_collections_rv.layoutParams = param
   */

        val charset = ('a'..'z')
        presenter.callSearchPhotoApiList(charset.random().toString(), ACCESS_API_KEY)
        requireActivity().search_edit.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                requireActivity().search_frag_empty_const.visibility = View.GONE
                main_collections_rv.visibility = View.VISIBLE
                searchTxt = s.toString()

                if (requireActivity().search_edit.text.isNotEmpty()) {
                    presenter.callSearchPhotoApiList(searchTxt, ACCESS_API_KEY)
                } else {
                    presenter.callSearchPhotoApiList(charset.random().toString(), ACCESS_API_KEY)
                }
            }

        })
    }

    override fun loadSearchPhotoApiPhotoList(data: SearchPhotoModel) {
        if (data.results.isEmpty()) {
            requireActivity().search_frag_empty_const.visibility = View.VISIBLE
            requireActivity().search_frag_resultOfSearch.text = "This photo is not available"
            main_collections_rv.visibility = View.GONE
        }
        searchPhotoListModels.clear()
        searchPhotoListModels.addAll(data.results)
        if (this.isAdded){
        main_collections_rv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = recyclerSearchPhotoAdapter
        }
        }
    }

    override fun loadSearchCollectionsApiList(data: SearchCollectionModel) {
    }

    override fun checkNetWorkConnection(): Boolean {
        return requireContext().isNetworkAvailable()
    }

    override fun errorNetWorkConnection() {

        Toast.makeText(requireContext(), "Error network connection", Toast.LENGTH_LONG).show()
    }

    override fun responseCodeError() {

    }

    override fun responseError(error: Throwable) {

        Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
    }

    override fun serverError(error: String) {

        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
    }

    override fun showLoader() {
        if (this.isAdded) {
            requireActivity().search_collectionFrag_progress.visibility = View.VISIBLE
        }
    }

    override fun hideLoader() {

        if (this.isAdded) {
            requireActivity().search_collectionFrag_progress.visibility = View.GONE
        }
    }

    @Subscribe
    fun onReceiveId(event: EventSendId.OnSendSearchIdToDetails) {

        val detailsFragment = DetailsFragment()
        val manager: FragmentManager = requireActivity().supportFragmentManager
        val transaction = manager.beginTransaction()
        val bundle = Bundle()
        id = event.id
        profileUrl = event.profileUrl
        pageFlag = event.backFlag
        bundle.putString(PHOTO_ID, id)
        bundle.putString(PROFILE_URL_IMG, profileUrl)
        bundle.putString(BACK_PAGE_FLAG, pageFlag)
        detailsFragment.arguments = bundle
        transaction.replace(R.id.search_fragmentContainerView, detailsFragment, null)
        requireActivity().Search_const.visibility = View.GONE
        requireActivity().search_fragmentContainerView.visibility = View.VISIBLE
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)

    }
}