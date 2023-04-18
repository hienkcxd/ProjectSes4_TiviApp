package com.example.projectses4_apptivi.io.service

import android.util.Log
import com.example.projectses4_apptivi.io.AuthInterceptor
import com.example.projectses4_apptivi.io.response.AreaResponse
import com.example.projectses4_apptivi.io.response.GroupStoreResponse
import com.example.projectses4_apptivi.io.response.LoginResponse
import com.example.projectses4_apptivi.io.response.StoreResponse
import com.example.projectses4_apptivi.model.DeviceModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DeviceService {
    @GET(value = "user/area")
    fun getArea(
        @Header("Authorization") token: String,
    ): Call<List<AreaResponse>>

    //lay danh sach thiet bị
    @GET(value = "user/device")
    fun getDeviceList(
        @Header("Authorization") token: String,
    ): Call<List<DeviceModel>>
    //lay danh sach thiet bị
    @GET(value = "user/device/dto={deviceId}")
    fun getDeviceDetail(
        @Header("Authorization") token: String,
        @Path("deviceId") deviceId: String
    ): Call<DeviceModel>
    @GET(value = "user/store")
    fun getStore(
        @Header("Authorization") token: String
    ): Call<List<StoreResponse>>

    @GET(value = "user/device-in-group")
    fun getGroupDevice(
        @Header("Authorization") token: String
    ): Call<List<GroupStoreResponse>>
    @POST(value = "user/device")
    fun saveDevice(
        @Header("Authorization") token: String,
        @Body device:DeviceModel
    ): Call<DeviceModel>


    companion object {
//        private const val BASE_URL = "http://192.168.1.128:8080/api/"
        private const val BASE_URL ="http://172.16.1.170:8080/api/"


        fun create(authToken: String): DeviceService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            Log.i("jwt", "gọi vào okHTTP ")
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(AuthInterceptor(authToken))
                .build()
            Log.i("jwt", "gọi vào retrofit ")
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(DeviceService::class.java)
        }
    }
}
