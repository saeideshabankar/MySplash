package com.example.mysplash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.mysplash.databinding.ActivityMainBinding
import com.example.mysplash.ui.main.home_frag.adapter.MainViewPagerAdapter
import com.example.mysplash.utils.EventSendId
import com.example.mysplash.utils.PREFS_IS_LIGHT_OR_DARK
import nouri.`in`.goodprefslib.GoodPrefs
import org.greenrobot.eventbus.EventBus


class MainActivity : AppCompatActivity() {

    private var lightOrDark = ""
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //set theme
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
                        this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
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

        //ViewPager and Tablayout setup
        val adapter = MainViewPagerAdapter(supportFragmentManager)
        binding.mainViewPager.adapter = adapter
        binding.mainTabLayout.setupWithViewPager(binding.mainViewPager)
        // binding.mainViewPager.offscreenPageLimit=adapter.count
        binding.mainViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }
            @SuppressLint("MissingSuperCall")
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                //EventBus for sending position to fragment (for sort dialog)
                EventBus.getDefault().post(
                    EventSendId.OnSendViewPagerPosition(
                        position))
            }
            override fun onPageSelected(position: Int) {
            }
        })

        //BottomNavigation codes
        binding.mainBottomNavBar.background = null
        binding.mainBottomNavBar.menu.getItem(2).isEnabled = false

        //Central fab in mainActivity function
        binding.mainPageFab.setOnClickListener {
            //  chatPage_txt.text = "افزودن پست"
            val url = "http://www.unsplash.com"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    private fun lightTheme() {
        binding.mainPageFab.setBackgroundColor(Color.BLACK)

        //change background for vector img( change color of plus button)
        binding.mainPageFab.setColorFilter(resources.getColor(R.color.white))

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

    private fun darkTheme() {

        binding.mainPageFab.setBackgroundColor(resources.getColor(R.color.white))
        binding.mainPageFab.setColorFilter(resources.getColor(R.color.black))
        binding.mainPageFab.backgroundTintList =
            ColorStateList.valueOf(resources.getColor(R.color.white))

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

    }
}