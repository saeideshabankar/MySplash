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
import com.example.mysplash.data.models.search_collections.Result
import com.example.mysplash.data.models.search_collections.SearchCollectionModel
import com.example.mysplash.data.models.search_photo.SearchPhotoModel
import com.example.mysplash.ui.main.one_collection_frag.OneCollectionFragment
import com.example.mysplash.ui.main.search_activity.adapter.SearchCollectionsAdapter
import com.example.mysplash.utils.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_collection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class SearchCollectionFragment : Fragment(),SearchApiContract.View{

    private var searchTxt = ""
    private  var backPageFlag: String=""
    private var enterPageFlag: String=""
    private var id: String=""
    private var title: String=""
    private val searchCollectionsListModels:MutableList<Result> = mutableListOf()

    private val presenter: SearchApiPresenterImpl by lazy { SearchApiPresenterImpl(this) }
    private val recyclerSearchPhotoAdapter: SearchCollectionsAdapter by lazy {
        SearchCollectionsAdapter(requireContext(), searchCollectionsListModels)
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

        requireActivity().Search_const.visibility= View.VISIBLE
        requireActivity().search_fragmentContainerView.visibility= View.GONE

        val charset = ('a'..'z')

        presenter.callSearchCollectionsApiList( charset.random().toString(), ACCESS_API_KEY)
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
                    presenter.callSearchCollectionsApiList(searchTxt, ACCESS_API_KEY)
                } else {
                    presenter.callSearchCollectionsApiList(charset.random().toString(), ACCESS_API_KEY)
                }
            }

        })
    }

    override fun loadSearchPhotoApiPhotoList(data: SearchPhotoModel) {

    }

    override fun loadSearchCollectionsApiList(data: SearchCollectionModel) {
        if (data.results.isEmpty()) {
            requireActivity().search_frag_empty_const.visibility = View.VISIBLE
            requireActivity().search_frag_resultOfSearch.text = "This photo is not available"
            main_collections_rv.visibility = View.GONE
        }
        searchCollectionsListModels.clear()
        searchCollectionsListModels.addAll(data.results)
        main_collections_rv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = recyclerSearchPhotoAdapter
        }
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
        requireActivity().search_collectionFrag_progress?.let {
            it.visibility = View.VISIBLE
        }
    }

    override fun hideLoader() {
        requireActivity().search_collectionFrag_progress?.let {
            it.visibility = View.GONE
        }
    }


    @Subscribe
    fun onReceiveId(event: EventSendId.OnSendIdToOneCollection) {
        val oneCollectionFragment = OneCollectionFragment()
        val manager: FragmentManager = requireActivity().supportFragmentManager
        val transaction = manager.beginTransaction()
        val bundle = Bundle()
         id = event.id
         title = event.title
         backPageFlag=event.backFlag
         enterPageFlag=event.enterFlag
        bundle.putString("PHOTO_ID_TO_ONE", id)
        bundle.putString(TITLE, title)
        bundle.putString(BACK_PAGE_FLAG, backPageFlag)
        bundle.putString(ENTER_PAGE_FLAG, enterPageFlag)
        oneCollectionFragment.arguments = bundle
        transaction.add(
            R.id.search_fragmentContainerView,
            oneCollectionFragment,
            ""
        )
        requireActivity().Search_const.visibility= View.GONE
        requireActivity().search_fragmentContainerView.visibility= View.VISIBLE
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