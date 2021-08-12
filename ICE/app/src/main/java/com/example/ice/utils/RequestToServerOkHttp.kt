package com.example.ice.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestToServerOkHttp {

    private const val BASE_URL = "http://118.67.135.89:8000"
    private val okHttpClient = OkHttpClient.Builder().build()
    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
    var service: RequestInterface = retrofit.create(RequestInterface::class.java)

}