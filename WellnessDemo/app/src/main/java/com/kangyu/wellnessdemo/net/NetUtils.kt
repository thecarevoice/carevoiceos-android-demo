package com.kangyu.wellnessdemo.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext

object NetUtils {

   private var retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl(getBaseUrl())
       .client(createOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getBaseUrl(): String {
        return "https://p2-stag.kangyu.info/"
    }

    fun getApiService(): ApiService {
        return retrofit?.create(ApiService::class.java)!!
    }

    // 信任所有证书的配置
    private val trustAllCerts = arrayOf<javax.net.ssl.TrustManager>(
        object : javax.net.ssl.X509TrustManager {
            override fun checkClientTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
        }
    )

    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // 抓包级别：BODY
        }

        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        // 添加抓包证书支持
        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as javax.net.ssl.X509TrustManager)
            builder.hostnameVerifier(javax.net.ssl.HostnameVerifier { _, _ -> true })
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return builder.build()
    }
}