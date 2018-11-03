package com.example.cam.mealhc_v4

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    val PREFS_FILENAME = "com.example.cam.prefs"
    val DAIRY = "No Dairy"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var dairyAllergy: Boolean
        get() = prefs.getBoolean(DAIRY, false)
        set(value) = prefs.edit().putBoolean(DAIRY, value).apply()
}