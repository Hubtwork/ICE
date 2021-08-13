package com.example.ice.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RequestToServerOkHttp {

    private const val BASE_URL = "http://118.67.135.89:8000"
    private fun generateOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.connectTimeout(15, TimeUnit.SECONDS)
        client.readTimeout(15, TimeUnit.SECONDS)
        return client.build()
    }
    private val okHttpClient = {
        val client = OkHttpClient.Builder().build()
        client
    }
    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(
        generateOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create()).build()
    var service: RequestInterface = retrofit.create(RequestInterface::class.java)

}