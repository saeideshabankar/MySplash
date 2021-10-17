package com.example.mysplash.utils

import android.app.Application
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import io.github.inflationx.calligraphy3.BuildConfig
import nouri.`in`.goodprefslib.GoodPrefs


class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        GoodPrefs.init(applicationContext)
       //For set font
        /* ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/roboto.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )*/

        val config = YandexMetricaConfig.newConfigBuilder(CRASH_API_KEY)
            .withAppVersion(BuildConfig.VERSION_NAME)
            .withCrashReporting(true)
            .withNativeCrashReporting(true)
            .build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)


        //settigs for PR download
        val prDownloaderConfig = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()
        PRDownloader.initialize(applicationContext, prDownloaderConfig)

    }
}