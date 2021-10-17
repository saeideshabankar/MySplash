package com.example.mysplash.ui.main.collection_frag

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysplash.R
import com.example.mysplash.data.models.photo_model.CollectionModel
import com.example.mysplash.databinding.FragmentCollectionBinding
import com.example.mysplash.ui.main.collection_frag.adapter.RecyclerCollectionAdapter
import com.example.mysplash.ui.main.one_collection_frag.OneCollectionFragment
import com.example.mysplash.ui.main.search_activity.SearchActivity
import com.example.mysplash.ui.main.settings.SettingsActivity
import com.example.mysplash.utils.*
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mazenrashed.MenuBottomSheet
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class CollectionFragment : Fragment(),CollectionApiContract.View {

    private lateinit var binding: FragmentCollectionBinding
    private val collectionListModels:MutableList<CollectionModel> = mutableListOf()
   private var title=""
   private var id=""
   private var backPageFrag =""
   private var enterPageFrag =""
    private lateinit var menuBottom: MenuBottomSheet
    private val collectionPresenter: CollectionApiPresenterImpl by lazy { CollectionApiPresenterImpl(this) }
    private val recyclerCollectionAdapter: RecyclerCollectionAdapter by lazy {
        RecyclerCollectionAdapter(requireContext(), collectionListModels)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCollectionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectionPresenter.callCollectionsApi(ACCESS_API_KEY)

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
        activity?.findViewById<BottomNavigationView>(R.id.main_bottomNavBar)?.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_menu -> {

                    menuBottom.show(this)
                }
                R.id.menu_search -> {
                    requireActivity().intentForEveryWhere(SearchActivity::class.java)
                }
                R.id.menu_sort -> {

                   /* val dialog = Dialog(requireContext(), android.R.style.ThemeOverlay_Material_Dialog_Alert)
                    dialog.setContentView(R.layout.sort_collection_dialog)

                    val sortRadioGrpDialog = dialog.findViewById(R.id.sort_radio_group) as RadioGroup

                    dialog.show()*/
                }
            }
            true }

    }

    override fun loadCollectionsApi(data: MutableList<CollectionModel>) {


        collectionListModels.clear()
        collectionListModels.addAll(data)

        if (this.isAdded) {
            binding.mainCollectionsRv.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = recyclerCollectionAdapter
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
        Log.e("TAG", "responseError: $error")
    }

    override fun serverError(error: String) {

        Toast.makeText(requireContext(), "Server has error ", Toast.LENGTH_SHORT).show()
    }
    override fun showLoader() {
        /*Handler().postDelayed({
            activity?.findViewById<ProgressBar>(R.id.main_collectionFrag_progress)?.visibility =
                View.VISIBLE

       }, DETAILS_HANDLER)*/
    }

    override fun hideLoader() {

        if (this.isAdded){
            activity?.findViewById<ProgressBar>(R.id.main_collectionFrag_progress)?.visibility =
                View.GONE
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
         backPageFrag = event.backFlag
         enterPageFrag = event.enterFlag
        // Toast.makeText(this, "event.titke${event.title}", Toast.LENGTH_SHORT).show()
        bundle.putString("PHOTO_ID_TO_ONE", id)
        bundle.putString(TITLE, title)
        bundle.putString(BACK_PAGE_FLAG, backPageFrag)
        bundle.putString(ENTER_PAGE_FLAG, enterPageFrag)
        oneCollectionFragment.arguments = bundle
       transaction.add(
            R.id.main_fragmentContainerView,
            oneCollectionFragment,
            ""
        )
        activity?.findViewById<FragmentContainerView>(R.id.main_fragmentContainerView)?.visibility =
            View.VISIBLE
        activity?.findViewById<BottomAppBar>(R.id.mainPage_bottomAppBar)?.visibility =
            View.GONE
        activity?.findViewById<FloatingActionButton>(R.id.mainPage_fab)?.visibility =
            View.GONE
        activity?.findViewById<ConstraintLayout>(R.id.main_tabCons)?.visibility =
            View.GONE

        transaction.addToBackStack(null)
        transaction.commit()
    }
    @Subscribe
    fun onReceiveId(event: EventSendId.OnSendViewPagerPosition) {


        if (event.position==1){
            activity?.findViewById<BottomNavigationView>(R.id.main_bottomNavBar)?.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_menu -> {

                        menuBottom.show(this)
                    }
                    R.id.menu_search -> {
                        requireActivity().intentForEveryWhere(SearchActivity::class.java)
                    }
                    R.id.menu_sort -> {

                        val dialog = Dialog(requireContext(), android.R.style.ThemeOverlay_Material_Dialog_Alert)
                        dialog.setContentView(R.layout.sort_collection_dialog)

                        val sortRadioGrpDialog = dialog.findViewById(R.id.sort_radio_group) as RadioGroup

                        dialog.show()
                    }
                }
                true }
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
//    //this method shows us when viewpager is runing again ,for update sort dialog
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
//                                val dialog = Dialog(requireContext(), android.R.style.ThemeOverlay_Material_Dialog_Alert)
//                                dialog.setContentView(R.layout.sort_collection_dialog)
//
//                                val sortRadioGrpDialog = dialog.findViewById(R.id.sort_radio_group) as RadioGroup
//
//                                dialog.show()
//                            }
//                        }
//                        true }
//                }
//            },500)
//        }
  //  }


}