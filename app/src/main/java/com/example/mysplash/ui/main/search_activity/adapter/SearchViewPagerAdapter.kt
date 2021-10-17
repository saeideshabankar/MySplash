package com.example.mysplash.ui.main.search_activity.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mysplash.ui.main.search_activity.SearchCollectionFragment
import com.example.mysplash.ui.main.search_activity.SearchPhotoFragment

class SearchViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm,lifecycle) {

    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> {
                return SearchPhotoFragment()
            }
            1 -> {
                return SearchCollectionFragment()
            }
            else -> {
                return SearchPhotoFragment()
            }
        }
    }

    override fun getItemCount(): Int {
     return 2
    }
}