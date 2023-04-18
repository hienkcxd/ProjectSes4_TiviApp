package com.example.projectses4_apptivi.io.service

import com.example.projectses4_apptivi.io.response.LoginResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface VideoService {
    @GET(value = "login")
    fun getVideo(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    companion object ApiLogin{
        //        private const val BASE_URL = "http://192.168.1.128:8080/api/"
        private const val BASE_URL ="http://172.16.1.170:8080/api/"
        fun create():VideoService{
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(VideoService::class.java)
        }
    }
}