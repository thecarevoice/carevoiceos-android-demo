package com.kangyu.wellnessdemo.net.bean

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val app: Any?,
    val sdk: Sdk
)
data class Sdk(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
