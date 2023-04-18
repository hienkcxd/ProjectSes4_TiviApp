package com.example.projectses4_apptivi.io.ManageSharePreference

import android.content.Context
import android.content.SharedPreferences

class VideoListSharePreference(context: Context) {
    companion object {
        private const val PREFS_NAME_VIDEO = "video_prefs"
        private const val VIDEO_NAME = "videoName"
        private const val URI_VIDEO = "uriVideo"
    }
    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME_VIDEO, Context.MODE_PRIVATE)
    fun saveVideo(videoName: String) {
        preferences.edit().putString(VIDEO_NAME, videoName).apply()
    }
    fun saveUri(uriVideo: String) {
        preferences.edit().putString(URI_VIDEO, uriVideo).apply()
    }
    fun getVideoName() : String? {
        return preferences.getString(VIDEO_NAME, null)
    }

    fun getVideoUri() : String? {
        return preferences.getString(URI_VIDEO, null)
    }
    fun clearVideoName() {
        preferences.edit().remove(VIDEO_NAME).apply()
    }

    fun clearVideoUri() {
        preferences.edit().remove(URI_VIDEO).apply()
    }
}