package com.desaysv.aicockpit.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.Locale

/**
 * 负责切换语言，更新配置，保存当前语言。
 */
object LocaleManager {
    private const val PREF_NAME = "LocalePrefs"
    private const val KEY_LANGUAGE = "language"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getLanguage(): String {
        return sharedPreferences.getString(KEY_LANGUAGE, Locale.getDefault().language) ?: Locale.getDefault().language
    }

    //基本用不到
    fun setLanguage(context: Context, language: String) {
        sharedPreferences.edit().putString(KEY_LANGUAGE, language).apply()

        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        context.createConfigurationContext(config)
    }

    fun isEn():Boolean{
        return getLanguage()=="en"
    }
}
