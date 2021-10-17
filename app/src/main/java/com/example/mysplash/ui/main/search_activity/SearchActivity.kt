package com.example.mysplash.ui.main.search_activity

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mysplash.R
import com.example.mysplash.databinding.ActivitySearchBinding
import com.example.mysplash.ui.main.search_activity.adapter.SearchViewPagerAdapter
import com.example.mysplash.utils.PREFS_IS_LIGHT_OR_DARK
import com.google.android.material.tabs.TabLayoutMediator
import nouri.`in`.goodprefslib.GoodPrefs




class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private var lightOrDark=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tabsArray = arrayOf(getString(R.string.collection), getString(R.string.photo))

        binding.searchBack.setOnClickListener {
            this.onBackPressed()
        }

        //ViewPager and Tablayout setup
        val adapter = SearchViewPagerAdapter(supportFragmentManager, lifecycle)
            binding.searchViewPager.adapter = adapter
        TabLayoutMediator(binding.searchTabLayout, binding.searchViewPager) { tab, position ->
            tab.text = tabsArray[position]
        }.attach()
        //Good prefs if theme is dark back or light tint must be changed for back bnt
        if (GoodPrefs.getInstance().isKeyExists(PREFS_IS_LIGHT_OR_DARK)) {
             lightOrDark = GoodPrefs.getInstance().getString(PREFS_IS_LIGHT_OR_DARK, "")
            when (lightOrDark) {
                "Light" -> {

                    lightTheme()
                }
                "Dark" -> {
                    darkTheme()

                }
                "System default" -> {
                    //flw system
                    val nightModeFlags: Int =
                        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                    when (nightModeFlags) {
                        //Night
                        Configuration.UI_MODE_NIGHT_YES -> {
                            darkTheme()
                        }
                        Configuration.UI_MODE_NIGHT_NO -> {
                            lightTheme()
                        }
                    }
                }
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //when u back pressed close the keyboard
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    private fun lightTheme(){
        //change color hint
        binding.searchBack.setColorFilter(resources.getColor(R.color.black))
        binding.searchFragEmptyBoxImg.setColorFilter(resources.getColor(R.color.black))
        binding.searchFragResultOfSearch.setTextColor(Color.BLACK)
        binding.searchEdit.setHintTextColor(Color.BLACK)
        //change stutus bar  text color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }


    }
    private fun darkTheme(){
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        binding.searchBack.setColorFilter(resources.getColor(R.color.white))
        binding.searchFragEmptyBoxImg.setColorFilter(resources.getColor(R.color.white))
        binding.searchFragResultOfSearch.setTextColor(Color.WHITE)
        binding.searchEdit.setHintTextColor(Color.WHITE)
    }


}
