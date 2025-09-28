package com.kangyu.wellnessdemo.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetUtils {

   private var retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl(getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getBaseUrl(): String {
        return "https://apis.carevoiceos.com"
    }

    fun getApiService(): ApiService {
        return retrofit?.create(ApiService::class.java)!!
    }

}