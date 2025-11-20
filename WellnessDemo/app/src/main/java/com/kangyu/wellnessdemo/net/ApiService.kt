package com.kangyu.wellnessdemo.net

import DeepLinkBean
import DeepLinkBeanResponse
import com.carevoice.lib_common.net.entity.BaseResponse
import com.kangyu.wellnessdemo.net.bean.DeepLinkRequest
import com.kangyu.wellnessdemo.net.bean.DeepLinkResponse
import com.kangyu.wellnessdemo.net.bean.LoginRequest
import com.kangyu.wellnessdemo.net.bean.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/api/app/auth/register")
    suspend fun register(@Body data: LoginRequest): BaseResponse<LoginResponse>

    @POST("/api/app/auth/login")
    suspend fun login(@Body data: LoginRequest): BaseResponse<LoginResponse>

    @POST("/api/app/auth/deep-link")
    suspend fun deepLink(@Body data: DeepLinkBean): BaseResponse<DeepLinkBeanResponse>

    @POST("/api/app/auth/validate-deep-link")
    suspend fun validateDeepLink(@Body deepLinkRequest: DeepLinkRequest): BaseResponse<DeepLinkResponse>

}