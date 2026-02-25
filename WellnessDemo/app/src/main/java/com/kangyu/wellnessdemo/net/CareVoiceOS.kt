package com.kangyu.wellnessdemo.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CareVoiceOS {
    fun getBaseUrl(): String {
        return "https://apis.carevoiceos.com/"
    }
}