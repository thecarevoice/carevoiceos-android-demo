package com.kangyu.wellnessdemo.net

import com.carevoice.lib_common.net.entity.BaseResponse
import com.kangyu.wellnessdemo.net.bean.LoginRequest
import com.kangyu.wellnessdemo.net.bean.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/api/app/auth/register")
    suspend fun register(@Body data: LoginRequest): BaseResponse<LoginResponse>

    @POST("/api/app/auth/login")
    suspend fun login(@Body data: LoginRequest): BaseResponse<LoginResponse>

}