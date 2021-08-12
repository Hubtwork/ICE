package com.example.ice.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestToServer {

    var retrofit = Retrofit.Builder()
        .baseUrl("http://118.67.135.89:8000") .addConverterFactory(GsonConverterFactory.create()) .build()
    var service: RequestInterface = retrofit.create(RequestInterface::class.java)

}