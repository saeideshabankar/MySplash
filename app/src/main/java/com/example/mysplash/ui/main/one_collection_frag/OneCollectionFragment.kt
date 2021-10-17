package com.example.mysplash.ui.main.one_collection_frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysplash.R
import com.example.mysplash.data.models.photo_model.one_collection.OneCollectionModel
import com.example.mysplash.databinding.FragmentOneCollectionBinding
import com.example.mysplash.utils.*
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import nouri.`in`.goodprefslib.GoodPrefs

class OneCollectionFragment : Fragment(), OneCollectionApiContract.View {
   private  var backPageFrag: String=""
    private var enterPageFrag: String=""
    private var photoId: String=""
    private var photoTitle: String=""
    private val list: MutableList<OneCollectionModel> = mutableListOf()

    private lateinit var binding: FragmentOneCollectionBinding
    private val presenterImpl: OneCollectionApiPresenterImpl by lazy {
        OneCollectionApiPresenterImpl(
            this
        )
    }
    private val recyclerOneCollectionPhotoAdapter: OneCollectionAdapter by lazy {
        OneCollectionAdapter(requireContext(), enterPageFrag, list)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOneCollectionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         photoId = arguments?.getString("PHOTO_ID_TO_ONE").toString()
         photoTitle = arguments?.getString(TITLE).toString()
        backPageFrag = arguments?.getString(BACK_PAGE_FLAG).toString()
        enterPageFrag = arguments?.getString(ENTER_PAGE_FLAG).toString()


        if (photoTitle != null) {
            presenterImpl.callOneCollectionApiPhotoList(photoId, ACCESS_API_KEY)
                binding.toolbarTitle1.text = photoTitle
        } else {
            Toast.makeText(requireContext(), "ur arguments data is empty", Toast.LENGTH_SHORT)
                .show()
        }
            binding.toolbarBack1.setOnClickListener {

            if (backPageFrag == COLLECTION_FRAG) {

                activity?.findViewById<FragmentContainerView>(R.id.main_fragmentContainerView)?.visibility =
                    View.GONE
                activity?.findViewById<BottomAppBar>(R.id.mainPage_bottomAppBar)?.visibility =
                    View.VISIBLE
                activity?.findViewById<FloatingActionButton>(R.id.mainPage_fab)?.visibility =
                    View.VISIBLE
                activity?.findViewById<ConstraintLayout>(R.id.main_tabCons)?.visibility =
                    View.VISIBLE
            } else {
                activity?.findViewById<ConstraintLayout>(R.id.Search_const)?.visibility =
                    View.VISIBLE
                activity?.findViewById<FragmentContainerView>(R.id.search_fragmentContainerView)?.visibility =
                    View.GONE
            }
            requireActivity().onBackPressed()
        }

        //Good prefs if theme is dark back or light tint must be changed for back bnt
        if (GoodPrefs.getInstance().isKeyExists(PREFS_IS_LIGHT_OR_DARK)) {
            val lightOrDark = GoodPrefs.getInstance().getString(PREFS_IS_LIGHT_OR_DARK, "")
            if (lightOrDark == "Light") {
                binding.toolbarBack1.setColorFilter(resources.getColor(R.color.black))
                    binding.toolbarBrowser1.setColorFilter(resources.getColor(R.color.black))
                        binding.toolbarMore1.setColorFilter(resources.getColor(R.color.black))

            } else {
                binding.toolbarBack1.setColorFilter(resources.getColor(R.color.white))
                binding.toolbarBrowser1.setColorFilter(resources.getColor(R.color.white))
                binding.toolbarMore1.setColorFilter(resources.getColor(R.color.white))
            }
        } else {
            Toast.makeText(requireContext(), "theme doesnt set", Toast.LENGTH_SHORT).show()

        }
    }

    override fun loadOneCollectionApiPhotoList(data: MutableList<OneCollectionModel>) {


        if (data.isEmpty()) {
            Toast.makeText(requireContext(), "data is empty", Toast.LENGTH_SHORT).show()
        } else {
            list.clear()
            list.addAll(data)
            val oneCollectionRv = binding.oneCollectionRv
            if (this.isAdded) {
                oneCollectionRv.apply {
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    adapter = recyclerOneCollectionPhotoAdapter
                }
            }
        }
    }

    override fun checkNetWorkConnection(): Boolean {
        return requireContext().isNetworkAvailable()
    }

    override fun errorNetWorkConnection() {

        Toast.makeText(requireContext(), "Error network connection", Toast.LENGTH_SHORT).show()
    }

    override fun responseCodeError() {

    }

    override fun responseError(error: Throwable) {

    }

    override fun serverError(error: String) {

        Toast.makeText(requireContext(), "Server has error ", Toast.LENGTH_SHORT).show()
    }

    override fun showLoader() {
        binding.oneCollectionFragProgress.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.oneCollectionFragProgress.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        if (backPageFrag == COLLECTION_FRAG) {

            activity?.findViewById<FragmentContainerView>(R.id.main_fragmentContainerView)?.visibility =
                View.GONE
            activity?.findViewById<BottomAppBar>(R.id.mainPage_bottomAppBar)?.visibility =
                View.VISIBLE
            activity?.findViewById<FloatingActionButton>(R.id.mainPage_fab)?.visibility =
                View.VISIBLE
            activity?.findViewById<ConstraintLayout>(R.id.main_tabCons)?.visibility =
                View.VISIBLE
        } else {
            activity?.findViewById<ConstraintLayout>(R.id.Search_const)?.visibility =
                View.VISIBLE
            activity?.findViewById<FragmentContainerView>(R.id.search_fragmentContainerView)?.visibility =
                View.GONE
        }
    }
}