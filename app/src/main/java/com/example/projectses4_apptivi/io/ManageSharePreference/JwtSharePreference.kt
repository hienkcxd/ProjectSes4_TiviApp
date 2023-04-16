package com.example.projectses4_apptivi.io.ManageSharePreference

import android.content.Context
import android.content.SharedPreferences

class JwtSharePreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "jwt_prefs"
        private const val JWT_KEY = "jwt"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveJwt(jwt: String) {
        preferences.edit().putString(JWT_KEY, jwt).apply()
    }

    fun getJwt(): String? {
        return preferences.getString(JWT_KEY, null)
    }

    fun clearJwt() {
        preferences.edit().remove(JWT_KEY).apply()
    }
}