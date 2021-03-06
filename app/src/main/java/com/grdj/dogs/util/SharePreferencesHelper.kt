package com.grdj.dogs.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.Preference
import androidx.preference.PreferenceManager

class SharePreferencesHelper {
    companion object{

        private const val PREF_TIME = "Pref time"
        private var prefs: SharedPreferences? = null
        @Volatile private var instance: SharePreferencesHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharePreferencesHelper = instance ?: synchronized(LOCK){
            instance ?: buildHelper(context).also{
                instance = it
            }
        }

        private fun buildHelper(context: Context):SharePreferencesHelper{
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharePreferencesHelper()
        }
    }

    fun saveUpdateTime(time: Long){
        prefs?.edit(commit = true){
            putLong(PREF_TIME, time)
        }
    }

    fun getUpdateTime() = prefs?.getLong(PREF_TIME, 0)

}