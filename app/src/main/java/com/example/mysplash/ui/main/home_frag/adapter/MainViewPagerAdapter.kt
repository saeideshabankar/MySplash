package com.example.mysplash.ui.main.home_frag.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.mysplash.ui.main.collection_frag.CollectionFragment
import com.example.mysplash.ui.main.home_frag.HomeFragment


class MainViewPagerAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm){
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> {

                HomeFragment()
            }
            1 -> {

                CollectionFragment()
            }
            else -> {
                HomeFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "HOME"
            }
            1 -> {
                return "COLLECTION"
            }
        }
        return super.getPageTitle(position)
    }

}
/*
class MainViewPagerAdapter(fm: FragmentManager) : FragmentStateAdapter(fm) {

    private val numTabs=2
    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> {
                return HomeFragment()
            }
            1 -> {
                return CollectionFragment()
            }
            else -> {
                return HomeFragment()
            }
        }
    }

    override fun getItemCount(): Int {
        return numTabs
    }

}*/