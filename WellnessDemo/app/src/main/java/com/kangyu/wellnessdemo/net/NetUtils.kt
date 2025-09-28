package com.kangyu.wellnessdemo.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetUtils {

   private var retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl(getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getBaseUrl(): String {
        return "http://10.0.2.2:3005"
    }

    fun getApiService(): ApiService {
        return retrofit?.create(ApiService::class.java)!!
    }

}