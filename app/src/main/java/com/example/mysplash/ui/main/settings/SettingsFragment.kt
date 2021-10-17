package com.example.mysplash.ui.main.settings

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.mysplash.R
import com.example.mysplash.databinding.FragmentSettingsBinding
import com.example.mysplash.utils.EventSendId
import com.example.mysplash.utils.PREFS_IS_EN_OR_FA
import com.example.mysplash.utils.PREFS_IS_LIGHT_OR_DARK
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.settings_lang_dialog.*
import kotlinx.android.synthetic.main.settings_theme_dialog.*
import nouri.`in`.goodprefslib.GoodPrefs
import org.greenrobot.eventbus.EventBus
import java.util.*


class SettingsFragment : Fragment() {
    lateinit var lightOrDark :String
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentSettingToolbar.fragmentSettingToolbarTitle.text = getString(R.string.settings_frag_title_page)
//        String mystring = getResources().getString(R.string.mystring)
        val settingLangDialog = Dialog(requireContext(), android.R.style.ThemeOverlay_Material_Dialog_Alert)
        settingLangDialog.setContentView(R.layout.settings_lang_dialog)

        val settingThemeDialog =
            Dialog(requireContext(), android.R.style.ThemeOverlay_Material_Dialog_Alert)
        settingThemeDialog.setContentView(R.layout.settings_theme_dialog)

        binding.fragmentSettingToolbar.settingToolbarBack.setOnClickListener {


            activity?.findViewById<FragmentContainerView>(R.id.main_fragmentContainerView)?.visibility =
                View.GONE
            activity?.findViewById<BottomAppBar>(R.id.mainPage_bottomAppBar)?.visibility =
                View.VISIBLE
            activity?.findViewById<FloatingActionButton>(R.id.mainPage_fab)?.visibility =
                View.VISIBLE
            activity?.findViewById<ConstraintLayout>(R.id.main_tabCons)?.visibility =
                View.VISIBLE
            requireActivity().recreate()


        }
        if (GoodPrefs.getInstance().isKeyExists(PREFS_IS_LIGHT_OR_DARK)) {
             lightOrDark = GoodPrefs.getInstance().getString(PREFS_IS_LIGHT_OR_DARK, "")

            if (lightOrDark == "Light") {
                binding.fragmentSettingToolbar.settingToolbarBack.setColorFilter(resources.getColor(R.color.black))
                    binding.fragmentSettingG50.setBackgroundColor(resources.getColor(R.color.black))
                settingThemeDialog.fragment_setting_light_RB.isChecked=true
                settingThemeDialog.fragment_setting_dark_RB.isChecked=false
                settingThemeDialog.fragment_setting_systemDefault_RB.isChecked=false
            } else if (lightOrDark == "Dark"){
                binding.fragmentSettingToolbar.settingToolbarBack.setColorFilter(resources.getColor(R.color.white))
                binding.fragmentSettingG50.setBackgroundColor(resources.getColor(R.color.white))
                settingThemeDialog.fragment_setting_light_RB.isChecked=false
                settingThemeDialog.fragment_setting_systemDefault_RB.isChecked=false
                settingThemeDialog.fragment_setting_dark_RB.isChecked=true
            }
            else if (lightOrDark == "System default"){
                settingThemeDialog.fragment_setting_systemDefault_RB.isChecked=true
                settingThemeDialog.fragment_setting_dark_RB.isChecked=false
                settingThemeDialog.fragment_setting_dark_RB.isChecked=false

            }
            binding.fragmentSettingSaveThemeValue.text=lightOrDark

        } else {
            Toast.makeText(requireContext(), "No defult value", Toast.LENGTH_SHORT).show()
        }
        if (GoodPrefs.getInstance().isKeyExists(PREFS_IS_EN_OR_FA)) {
            val engOrPersian = GoodPrefs.getInstance().getString(PREFS_IS_EN_OR_FA, "")
            if (engOrPersian == "en") {
                binding.fragmentSettingSaveLangValue.text="English"
                settingLangDialog.fragment_setting_lang_dialog_Rb_eng.isChecked=true
                settingLangDialog.fragment_setting_lang_dialog_Rb_fa.isChecked=false
            } else {
                binding.fragmentSettingSaveLangValue.text= "فارسی"
                settingLangDialog.fragment_setting_lang_dialog_Rb_eng.isChecked=false
                settingLangDialog.fragment_setting_lang_dialog_Rb_fa.isChecked=true
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
                            //light mode
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                            save instance for everytime
                            GoodPrefs.getInstance().saveString(PREFS_IS_LIGHT_OR_DARK, "Light")

                           binding.fragmentSettingSaveThemeValue.text=lightOrDark
                            settingThemeDialog.dismiss()
                        }

                        R.id.fragment_setting_dark_RB -> {
                            GoodPrefs.getInstance().saveString(PREFS_IS_LIGHT_OR_DARK, "Dark")
                           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            binding.fragmentSettingSaveThemeValue.text=lightOrDark
                            settingThemeDialog.dismiss()
                        }
                        R.id.fragment_setting_systemDefault_RB -> {
                            GoodPrefs.getInstance().saveString(PREFS_IS_LIGHT_OR_DARK, "System default")
                           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                            binding.fragmentSettingSaveThemeValue.text=lightOrDark
                            settingThemeDialog.dismiss()
                        }
                        else ->{
                            GoodPrefs.getInstance().saveString(PREFS_IS_LIGHT_OR_DARK, "System default")
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                            binding.fragmentSettingSaveThemeValue.text=lightOrDark
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
                        else ->{
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
    private fun setAppLocale(lang:String){

        val locale =Locale(lang)
        Locale.setDefault(locale)
        val config =requireContext().resources.configuration
        config.setLocale(locale)
        requireContext().createConfigurationContext(config)
        requireContext().resources.updateConfiguration(config,requireContext().resources.displayMetrics)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragmentContainerView,SettingsFragment()).commit()
    }
    override fun onDestroy() {
        super.onDestroy()
        activity?.findViewById<FragmentContainerView>(R.id.main_fragmentContainerView)?.visibility =
            View.GONE
        activity?.findViewById<BottomAppBar>(R.id.mainPage_bottomAppBar)?.visibility =
            View.VISIBLE
        activity?.findViewById<FloatingActionButton>(R.id.mainPage_fab)?.visibility =
            View.VISIBLE
        activity?.findViewById<ConstraintLayout>(R.id.main_tabCons)?.visibility =
            View.VISIBLE
        requireActivity().onBackPressed()

    }

}