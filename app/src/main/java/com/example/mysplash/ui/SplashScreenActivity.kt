package com.example.mysplash.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.mysplash.MainActivity
import com.example.mysplash.R
import com.example.mysplash.utils.PREFS_IS_EN_OR_FA
import com.example.mysplash.utils.PREFS_IS_LIGHT_OR_DARK
import com.example.mysplash.utils.SPLASH_TIME_OUT
import nouri.`in`.goodprefslib.GoodPrefs
import java.util.*

class SplashScreenActivity : AppCompatActivity() {

    private var lightOrDark = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        //change status bar color
        //toye in safe halate dark ham mikhaim sttsbar sefid va text meshki bashe
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }
        if (GoodPrefs.getInstance().isKeyExists(PREFS_IS_LIGHT_OR_DARK)) {

            lightOrDark = GoodPrefs.getInstance().getString(PREFS_IS_LIGHT_OR_DARK, "")
            when (lightOrDark) {
                "Light" -> {
                    lightTheme()
                }
                "Dark" ->{
                    darkTheme()
                }
                "System default" ->{
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
           /* if (lightOrDark == "Light") {
                lightTheme()
            } else if (lightOrDark == "Dark") {
                darkTheme()
            }*/
        } else {
            GoodPrefs.getInstance().saveString(PREFS_IS_LIGHT_OR_DARK, "System default")
            //flw system
            val nightModeFlags: Int =
                this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (nightModeFlags) {
                //Night
                Configuration.UI_MODE_NIGHT_YES -> {

                    GoodPrefs.getInstance().saveString(PREFS_IS_LIGHT_OR_DARK, "System default")
                    darkTheme()

                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    lightTheme()
                }
            }

        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            //set lang every time
            if (GoodPrefs.getInstance().isKeyExists(PREFS_IS_EN_OR_FA)) {
                val engOrPersian = GoodPrefs.getInstance().getString(PREFS_IS_EN_OR_FA, "")
                //  Toast.makeText(this, engOrPersian, Toast.LENGTH_SHORT).show()
                if (engOrPersian == "en") {
                    setAppLocale("en")
                } else {
                    setAppLocale("fa")
                }
            } else {
                GoodPrefs.getInstance().saveString(PREFS_IS_EN_OR_FA, "en")
                setAppLocale("en")
            }

            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }, SPLASH_TIME_OUT)


    }

    private fun setAppLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = this.resources.configuration
        config.setLocale(locale)
        this.createConfigurationContext(config)
        this.resources.updateConfiguration(config, this.resources.displayMetrics)
    }

    private fun lightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //change stutus bar  text color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
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
        //Night
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }


}
