package com.example.projectses4_apptivi.io.service

import android.util.Log
import com.example.projectses4_apptivi.io.response.LoginResponse
import com.google.gson.annotations.SerializedName
import org.checkerframework.checker.nullness.qual.KeyFor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface LoginService {
    @POST(value = "login")
    @FormUrlEncoded
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    companion object ApiLogin{
//        private const val BASE_URL = "http://192.168.1.128:8080/api/"
        private const val BASE_URL ="http://172.16.1.170:8080/api/"
        fun create():LoginService{
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(LoginService::class.java)
        }
    }
}
data class LoginRequest(
    @Field("username") val username: String,
    @Field("password") val password: String
)
