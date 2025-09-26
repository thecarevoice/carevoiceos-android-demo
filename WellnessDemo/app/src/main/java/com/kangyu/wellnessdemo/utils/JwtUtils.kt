package com.kangyu.wellnessdemo.utils

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.nio.charset.StandardCharsets

object JwtUtils {
    
    /**
     * 解析JWT token
     * @param token JWT token字符串
     * @return JWT payload的JsonObject，如果解析失败返回null
     */
    fun parseJwt(token: String): JsonObject? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) {
                return null
            }
            
            // 获取payload部分（第二部分）
            val payload = parts[1]
            
            // Base64解码
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING)
            val decodedString = String(decodedBytes, StandardCharsets.UTF_8)
            
            // 解析JSON
            val gson = Gson()
            gson.fromJson(decodedString, JsonObject::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * 从JWT token中获取tenant code
     * @param token JWT token字符串
     * @return tenant code，如果获取失败返回null
     */
    fun getTenantCodeFromJwt(token: String): String? {
        val jwtObj = parseJwt(token)
        return jwtObj?.get("tenant")?.asString
    }
}