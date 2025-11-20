package com.kangyu.wellnessdemo.net.bean

import com.google.gson.annotations.SerializedName

data class DeepLinkRequest(
    val authorizationCode: String,
    val codeVerifier: String
)

data class DeepLinkResponse(
    @SerializedName("access_token")
    val access_token: String,
    @SerializedName("refresh_token")
    val refresh_token: String,


    @SerializedName("token_type")
    val token_type: String,
    @SerializedName("expires_in")
    val expires_in: Long,


    )
