package com.example.ice.utils

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClient {
    private val BASE_URL = "http://118.67.135.89:8000"

    fun getRetrofitClient(context: Context): Retrofit {
        val okHttpClient = OkHttpClient.Builder().build()

        return Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}