package com.android.chat.managers

import android.content.Context
import android.content.SharedPreferences
import com.android.chat.utils.Chat.Companion.context

object PreferenceManager {

    private const val appPreferences = "Chat App Preferences"
    private const val userPreferences = "Chat User Preferences"

    fun getUserSharedPreferences(): SharedPreferences? {
        return context.getSharedPreferences(userPreferences, Context.MODE_PRIVATE)
    }

    fun getAppSharedPreferences(): SharedPreferences? {
        return context.getSharedPreferences(appPreferences, Context.MODE_PRIVATE)
    }

    fun clearAppPreferences() {
        getAppSharedPreferences()
            ?.edit()?.clear()?.apply()
    }

    fun clearUserPreferences() {
        getUserSharedPreferences()
            ?.edit()?.clear()?.apply()
    }

    fun getUserStringData(key: String): String? {
        return getUserSharedPreferences()
            ?.getString(key, null)
    }

    fun getUserBooleanData(key: String): Boolean? {
        return getUserSharedPreferences()
            ?.getBoolean(key, false)
    }

    fun getUserLongData(key: String): Long {
        return getUserSharedPreferences()
            ?.getLong(key, 0L) ?: 0L
    }

    fun getAppIntData(key: String): Int {
        return getAppSharedPreferences()
            ?.getInt(key, 0) ?: 0
    }

    fun getAppStringData(key: String): String? {
        return getAppSharedPreferences()
            ?.getString(key, null)
    }

    fun saveUserData(key: String, value: Any?) {
        val editor = getUserSharedPreferences()
            ?.edit()
        when (value) {
            is String -> editor?.putString(key, value)
            is Boolean -> editor?.putBoolean(key, value)
            is Float -> editor?.putFloat(key, value)
            is Int -> editor?.putInt(key, value)
            is Long -> editor?.putLong(key, value)
        }
        editor?.apply()
    }

    fun saveAppData(key: String, value: Any) {
        val editor = getAppSharedPreferences()
            ?.edit()
        when (value) {
            is String -> editor?.putString(key, value)
            is Boolean -> editor?.putBoolean(key, value)
            is Float -> editor?.putFloat(key, value)
            is Int -> editor?.putInt(key, value)
            is Long -> editor?.putLong(key, value)
        }
        editor?.apply()
    }

    fun removeUserKey(key: String) {
        val editor = getUserSharedPreferences()
            ?.edit()
        editor?.remove(key)
        editor?.apply()
    }

}