package com.example.mysplash.ui.main.settings

import android.app.Dialog
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.mysplash.R
import com.example.mysplash.databinding.FragmentSettingsBinding
import com.example.mysplash.utils.EventSendId
import com.example.mysplash.utils.PREFS_IS_EN_OR_FA
import com.example.mysplash.utils.PREFS_IS_LIGHT_OR_DARK
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.settings_lang_dialog.*
import kotlinx.android.synthetic.main.settings_theme_dialog.*
import nouri.`in`.goodprefslib.GoodPrefs
import org.greenrobot.eventbus.EventBus
import java.util.*

class SettingsActivity : AppCompatActivity() {
    lateinit var lightOrDark: String
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fragmentSettingToolbar.fragmentSettingToolbarTitle.text =
            getString(R.string.settings_frag_title_page)
//        String mystring = getResources().getString(R.string.mystring)
        val settingLangDialog = Dialog(this, android.R.style.ThemeOverlay_Material_Dialog_Alert)
        settingLangDialog.setContentView(R.layout.settings_lang_dialog)

        val settingThemeDialog =
            Dialog(this, android.R.style.ThemeOverlay_Material_Dialog_Alert)
        settingThemeDialog.setContentView(R.layout.settings_theme_dialog)

        binding.fragmentSettingToolbar.settingToolbarBack.setOnClickListener {

            this.onBackPressed()
        }
        if (GoodPrefs.getInstance().isKeyExists(PREFS_IS_LIGHT_OR_DARK)) {
            lightOrDark = GoodPrefs.getInstance().getString(PREFS_IS_LIGHT_OR_DARK, "")

            when (lightOrDark) {
                "Light" -> {
                    settingThemeDialog.fragment_setting_light_RB.isChecked = true
                    settingThemeDialog.fragment_setting_dark_RB.isChecked = false
                    settingThemeDialog.fragment_setting_systemDefault_RB.isChecked = false
                    lightTheme()
                }
                "Dark" -> {
                    darkTheme()
                     settingThemeDialog.fragment_setting_light_RB.isChecked = false
                    settingThemeDialog.fragment_setting_systemDefault_RB.isChecked = false
                    settingThemeDialog.fragment_setting_dark_RB.isChecked = true
                }
                "System default" -> {
                    settingThemeDialog.fragment_setting_systemDefault_RB.isChecked = true
                    settingThemeDialog.fragment_setting_dark_RB.isChecked = false
                    settingThemeDialog.fragment_setting_dark_RB.isChecked = false
                    //flw system
                    val nightModeFlags: Int =
                        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                    when (nightModeFlags) {
                        //Night
                        Configuration.UI_MODE_NIGHT_YES -> {
                            binding.fragmentSettingG50.setBackgroundColor(resources.getColor(R.color.white))
                            darkTheme()
                        }
                        Configuration.UI_MODE_NIGHT_NO -> {

                            binding.fragmentSettingG50.setBackgroundColor(resources.getColor(R.color.black))
                            lightTheme()
                        }
                    }
                }
            }
            binding.fragmentSettingSaveThemeValue.text = lightOrDark

        } else {
            Toast.makeText(this, "No defult value", Toast.LENGTH_SHORT).show()
        }
        if (GoodPrefs.getInstance().isKeyExists(PREFS_IS_EN_OR_FA)) {
            val engOrPersian = GoodPrefs.getInstance().getString(PREFS_IS_EN_OR_FA, "")
            if (engOrPersian == "en") {
                binding.fragmentSettingSaveLangValue.text = "English"
                settingLangDialog.fragment_setting_lang_dialog_Rb_eng.isChecked = true
                settingLangDialog.fragment_setting_lang_dialog_Rb_fa.isChecked = false
            } else {
                binding.fragmentSettingSaveLangValue.text = "فارسی"
                settingLangDialog.fragment_setting_lang_dialog_Rb_eng.isChecked = false
                settingLangDialog.fragment_setting_lang_dialog_Rb_fa.isChecked = true
            }
        } else {
            GoodPrefs.getInstance().saveString(PREFS_IS_EN_OR_FA, "en")
        }

        //Set Theme
        fragment_setting_theme_const.setOnClickListener {
            settingThemeDialog.fragment_setting_RG.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
                // on radio button check change
                when (checkedId) {
                    R.id.fragment_setting_light_RB -> {
                        //    save instance for everytime
                        GoodPrefs.getInstance().saveString(PREFS_IS_LIGHT_OR_DARK, "Light")

                        lightOrDark = GoodPrefs.getInstance().getString(PREFS_IS_LIGHT_OR_DARK, "")
                        binding.fragmentSettingSaveThemeValue.text = lightOrDark
                        Log.i("TAG", "onCreate: $lightOrDark")
                        //light mode
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        this.recreate()
                        settingThemeDialog.dismiss()
                    }

                    R.id.fragment_setting_dark_RB -> {
                        GoodPrefs.getInstance().saveString(PREFS_IS_LIGHT_OR_DARK, "Dark")
                        lightOrDark = GoodPrefs.getInstance().getString(PREFS_IS_LIGHT_OR_DARK, "")
                        binding.fragmentSettingSaveThemeValue.text = lightOrDark
                        Log.i("TAG", "onCreate: $lightOrDark")
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        settingThemeDialog.dismiss()
                    }
                    R.id.fragment_setting_systemDefault_RB -> {
                        GoodPrefs.getInstance().saveString(PREFS_IS_LIGHT_OR_DARK, "System default")
                        lightOrDark = GoodPrefs.getInstance().getString(PREFS_IS_LIGHT_OR_DARK, "")
                        //flw system
                        val nightModeFlags: Int =
                            this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                        when (nightModeFlags) {
                            //Night
                            Configuration.UI_MODE_NIGHT_YES -> {

                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                                settingThemeDialog.dismiss()
                                darkTheme()
                            }
                            Configuration.UI_MODE_NIGHT_NO -> {

                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                settingThemeDialog.dismiss()
                                lightTheme()
                            }
                        }
                        binding.fragmentSettingSaveThemeValue.text = lightOrDark
                        Log.i("TAG", "onCreate: $lightOrDark")
                    }
                    else -> {
                        GoodPrefs.getInstance().saveString(PREFS_IS_LIGHT_OR_DARK, "System default")
                        lightOrDark = GoodPrefs.getInstance().getString(PREFS_IS_LIGHT_OR_DARK, "")
                        binding.fragmentSettingSaveThemeValue.text = lightOrDark
                        Log.i("TAG", "onCreate: $lightOrDark")
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        settingThemeDialog.dismiss()
                    }
                }
            })
            settingThemeDialog.fragment_setting_cancel.setOnClickListener {
                settingThemeDialog.dismiss()

            }
            settingThemeDialog.show()
        }

        //Set lang
        binding.fragmentSettingLangConst.setOnClickListener {
            settingLangDialog.fragment_setting_lang_dialog_RG.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
                // on radio button check change
                when (checkedId) {
                    R.id.fragment_setting_lang_dialog_Rb_eng -> {
                        setAppLocale("en")
                        //save lang for every time
                        GoodPrefs.getInstance().saveString(PREFS_IS_EN_OR_FA, "en")
//                            ON SEND ID TO lang to main for start all  the where
                        EventBus.getDefault()
                            .post(EventSendId.OnSendLangToStartInMAin("en"))
                        settingLangDialog.dismiss()
                    }

                    R.id.fragment_setting_lang_dialog_Rb_fa -> {
                        setAppLocale("fa")
                        GoodPrefs.getInstance().saveString(PREFS_IS_EN_OR_FA, "fa")
//                            ON SEND ID TO lang to main for start all  the where
                        EventBus.getDefault()
                            .post(EventSendId.OnSendLangToStartInMAin("fa"))
                        settingLangDialog.dismiss()
                    }
                    else -> {
                        setAppLocale("en")
                        GoodPrefs.getInstance().saveString(PREFS_IS_EN_OR_FA, "en")
//                            ON SEND ID TO lang to main for start all  the where
                        EventBus.getDefault()
                            .post(EventSendId.OnSendLangToStartInMAin("en"))
                        settingLangDialog.dismiss()
                    }
                }
            })
            settingLangDialog.fragment_setting_lang_cancel.setOnClickListener {
                settingLangDialog.dismiss()

            }

            settingLangDialog.show()
        }

    }

    private fun setAppLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
        this.recreate()
    }

    private fun lightTheme() {
        binding.fragmentSettingToolbar.settingToolbarBack.setColorFilter(
            resources.getColor(
                R.color.black
            )
        )
        binding.fragmentSettingG50.setBackgroundColor(Color.BLACK)

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
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        binding.fragmentSettingToolbar.settingToolbarBack.setColorFilter(
            resources.getColor(
                R.color.white
            )
        )
        binding.fragmentSettingG50.setBackgroundColor(resources.getColor(R.color.white))

    }


}
