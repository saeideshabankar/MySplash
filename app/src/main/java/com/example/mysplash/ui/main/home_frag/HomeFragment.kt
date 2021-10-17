package com.example.mysplash.ui.main.home_frag

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysplash.R
import com.example.mysplash.data.models.photo_model.PhotoModel
import com.example.mysplash.databinding.FragmentCollectionBinding
import com.example.mysplash.ui.main.details_frag.DetailsFragment
import com.example.mysplash.ui.main.home_frag.adapter.RecyclerHomeAdapter
import com.example.mysplash.ui.main.search_activity.SearchActivity
import com.example.mysplash.ui.main.settings.SettingsActivity
import com.example.mysplash.utils.*
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mazenrashed.MenuBottomSheet
import kotlinx.android.synthetic.main.sort_home_dialog.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class HomeFragment : Fragment(), HomeApiContract.View {

    private val photoListModels: MutableList<PhotoModel> = mutableListOf()
    private lateinit var menuBottom: MenuBottomSheet
    private lateinit var binding: FragmentCollectionBinding
    private var sort = "latest"
    private var id = ""
    private var profileUrl = ""
    private var pageFlag = ""
    private var stringSort = "latest"
    private val presenter: HomeApiPresenterImpl by lazy { HomeApiPresenterImpl(this) }
    private val recyclerHomePhotoAdapter: RecyclerHomeAdapter by lazy {
        RecyclerHomeAdapter(requireContext(), photoListModels)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCollectionBinding.inflate(layoutInflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.callHomeApiPhotoList(ACCESS_API_KEY, sort)
        //InitMenu

        menuBottom = MenuBottomSheet.Builder()
            .setMenuRes(R.menu.main_bottom_sheet)
            .closeAfterSelect(true)
            .build()
        //SetMenu
        menuBottom.onSelectMenuItemListener = { position: Int, id: Int? ->
            when (id) {
                R.id.bottomSheetMenuSettingsThemeItem -> {

                    /* val settingsFragment = SettingsFragment()
                     val manager: FragmentManager = requireActivity().supportFragmentManager
                     val transaction = manager.beginTransaction()
                     transaction.replace(R.id.main_fragmentContainerView, settingsFragment, "")

                     activity?.findViewById<FragmentContainerView>(R.id.main_fragmentContainerView)?.visibility =
                         View.VISIBLE
                     activity?.findViewById<BottomAppBar>(R.id.mainPage_bottomAppBar)?.visibility =
                         View.GONE
                     activity?.findViewById<FloatingActionButton>(R.id.mainPage_fab)?.visibility =
                         View.GONE
                     activity?.findViewById<ConstraintLayout>(R.id.main_tabCons)?.visibility =
                         View.GONE

                     transaction.addToBackStack(null)
                     transaction.commit()*/
                    requireActivity().intentForEveryWhere(SettingsActivity::class.java)


                }
                R.id.bottomSheetMenuAboutItem -> Toast.makeText(
                    requireContext(),
                    "About",
                    Toast.LENGTH_SHORT
                ).show()
                //     R.id.menuProfile -> Toast.makeText(this, "کلیک روی منو پروفایل", Toast.LENGTH_SHORT).show()
            }
        }
        // requireActivity().main_bottomNavBar.selectedItemId = R.id.menu_menu
        activity?.findViewById<BottomNavigationView>(R.id.main_bottomNavBar)
            ?.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_menu -> {

                        menuBottom.show(this)
                    }
                    R.id.menu_search -> {
                        requireActivity().intentForEveryWhere(SearchActivity::class.java)
                    }
                    R.id.menu_sort -> {


                        val dialog =
                            Dialog(
                                requireContext(),
                                android.R.style.ThemeOverlay_Material_Dialog_Alert
                            )
                        dialog.setContentView(R.layout.sort_home_dialog)

                        dialog.dismiss()
                        dialog.sort_dialog_latest_btn.isChecked = true

                        val sortRadioGrpDialog =
                            dialog.findViewById(R.id.sort_radio_group) as RadioGroup
                        sortRadioGrpDialog.setOnCheckedChangeListener { _, checkedId ->
                            val radio = dialog.findViewById(checkedId) as RadioButton

                            when (radio.id) {
                                R.id.sort_dialog_latest_btn -> {
                                    stringSort = "latest"
                                    presenter.callHomeApiPhotoList(ACCESS_API_KEY, stringSort)
                                    dialog.dismiss()
                                }
                                R.id.sort_dialog_oldest_btn -> {
                                    stringSort = "oldest"
                                    presenter.callHomeApiPhotoList(ACCESS_API_KEY, stringSort)
                                    dialog.dismiss()
                                }
                                R.id.sort_dialog_popular_btn -> {
                                    stringSort = "popular"
                                    presenter.callHomeApiPhotoList(ACCESS_API_KEY, stringSort)
                                    dialog.dismiss()
                                }
                            }
                        }
                        dialog.show()
                    }
                }
                true
            }
    }
    override fun loadHomeApiPhotoList(data: MutableList<PhotoModel>) {

        photoListModels.clear()
        photoListModels.addAll(data)

        if (this.isAdded) {
            binding.mainCollectionsRv.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = recyclerHomePhotoAdapter
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
        if (this.isAdded) {
            activity?.findViewById<ProgressBar>(R.id.main_collectionFrag_progress)?.visibility =
                View.VISIBLE
        }
    }
    override fun hideLoader() {
        if (this.isAdded) {
            activity?.findViewById<ProgressBar>(R.id.main_collectionFrag_progress)?.visibility =
                View.GONE
        }
    }

    @Subscribe
    fun onReceiveId(event: EventSendId.OnSendIdToDetails) {

        val detailsFragment = DetailsFragment()
        val manager: FragmentManager = requireActivity().supportFragmentManager
        val transaction = manager.beginTransaction()
        val bundle = Bundle()
        id = event.id
        profileUrl = event.profileUrl
        pageFlag = event.pageFlag
        bundle.putString(PHOTO_ID, id)
        bundle.putString(PROFILE_URL_IMG, profileUrl)
        bundle.putString(BACK_PAGE_FLAG, pageFlag)
        detailsFragment.arguments = bundle
        transaction.replace(R.id.main_fragmentContainerView, detailsFragment, "")
        activity?.findViewById<FragmentContainerView>(R.id.main_fragmentContainerView)?.visibility =
            View.VISIBLE
        activity?.findViewById<BottomAppBar>(R.id.mainPage_bottomAppBar)?.visibility = View.GONE
        activity?.findViewById<FloatingActionButton>(R.id.mainPage_fab)?.visibility = View.GONE
        activity?.findViewById<ConstraintLayout>(R.id.main_tabCons)?.visibility = View.GONE
        transaction.addToBackStack(null)
        transaction.commit()
    }

    @Subscribe
    fun onReceiveId(event: EventSendId.OnSendViewPagerPosition) {


        if (event.position == 0) {
            // requireActivity().main_bottomNavBar.selectedItemId = R.id.menu_menu
            activity?.findViewById<BottomNavigationView>(R.id.main_bottomNavBar)
                ?.setOnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.menu_menu -> {

                            menuBottom.show(this)
                        }
                        R.id.menu_search -> {
                            requireActivity().intentForEveryWhere(SearchActivity::class.java)
                        }
                        R.id.menu_sort -> {

                            val dialog =
                                Dialog(
                                    requireContext(),
                                    android.R.style.ThemeOverlay_Material_Dialog_Alert
                                )
                            dialog.setContentView(R.layout.sort_home_dialog)

                            dialog.dismiss()
                            dialog.sort_dialog_latest_btn.isChecked = true

                            val sortRadioGrpDialog =
                                dialog.findViewById(R.id.sort_radio_group) as RadioGroup
                            sortRadioGrpDialog.setOnCheckedChangeListener { _, checkedId ->
                                val radio = dialog.findViewById(checkedId) as RadioButton

                                when (radio.id) {
                                    R.id.sort_dialog_latest_btn -> {
                                        stringSort = "latest"
                                        presenter.callHomeApiPhotoList(ACCESS_API_KEY, stringSort)
                                        dialog.dismiss()
                                    }
                                    R.id.sort_dialog_oldest_btn -> {
                                        stringSort = "oldest"
                                        presenter.callHomeApiPhotoList(ACCESS_API_KEY, stringSort)
                                        dialog.dismiss()
                                    }
                                    R.id.sort_dialog_popular_btn -> {
                                        stringSort = "popular"
                                        presenter.callHomeApiPhotoList(ACCESS_API_KEY, stringSort)
                                        dialog.dismiss()
                                    }
                                }
                            }
                            dialog.show()

                        }
                    }
                    true
                }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

//this method shows us when viewpager is runing again ,for update sort dialog
//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        if (isVisibleToUser){
//            Handler().postDelayed({
//                if (activity!=null){
//                    // requireActivity().main_bottomNavBar.selectedItemId = R.id.menu_menu
//                    activity?.findViewById<BottomNavigationView>(R.id.main_bottomNavBar)?.setOnNavigationItemSelectedListener { item ->
//                        when (item.itemId) {
//                            R.id.menu_menu -> {
//
//                                menuBottom.show(this)
//                            }
//                            R.id.menu_search -> {
//                                requireActivity().intentForEveryWhere(SearchActivity::class.java)
//                            }
//                            R.id.menu_sort -> {
//
//                                val dialog =
//                                    Dialog(requireContext(), android.R.style.ThemeOverlay_Material_Dialog_Alert)
//                                dialog.setContentView(R.layout.sort_home_dialog)
//
//                                dialog.dismiss()
//                                dialog.sort_dialog_latest_btn.isChecked=true
//
//                                val sortRadioGrpDialog =
//                                    dialog.findViewById(R.id.sort_radio_group) as RadioGroup
//                                sortRadioGrpDialog.setOnCheckedChangeListener { _, checkedId ->
//                                    val radio = dialog.findViewById(checkedId) as RadioButton
//
//                                    when (radio.id) {
//                                        R.id.sort_dialog_latest_btn -> {
//                                            stringSort = "latest"
//                                            presenter.callHomeApiPhotoList(ACCESS_API_KEY, stringSort)
//                                            dialog.dismiss()
//                                        }
//                                        R.id.sort_dialog_oldest_btn -> {
//                                            stringSort = "oldest"
//                                            presenter.callHomeApiPhotoList(ACCESS_API_KEY, stringSort)
//                                            dialog.dismiss()
//                                        }
//                                        R.id.sort_dialog_popular_btn -> {
//                                            stringSort = "popular"
//                                            presenter.callHomeApiPhotoList(ACCESS_API_KEY, stringSort)
//                                            dialog.dismiss()
//                                        }
//                                    }
//                                }
//                                dialog.show()
//
//                            }
//                        }
//                        true
//                    }
//                }
//            },500)
//        }
//    }
}