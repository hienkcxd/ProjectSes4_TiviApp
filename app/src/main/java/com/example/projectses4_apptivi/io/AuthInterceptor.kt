package com.example.projectses4_apptivi.io

import android.content.SharedPreferences
import com.example.projectses4_apptivi.io.ManageSharePreference.JwtSharePreference
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", token)
            .build()
        return chain.proceed(request)
    }
}