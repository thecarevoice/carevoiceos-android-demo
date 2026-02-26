package com.kangyu.wellnessdemo

import ai.asleep.asleepsdk.data.UserInfo
import android.app.Application
import com.carevoice.mindfulnesslibrary.Wellness
import java.util.Locale


class App: Application() {
    override fun onCreate() {
        super.onCreate()
        initSDK()
    }


    private fun initSDK() {
//        if (UserInfo.isLogin()) {
//            Wellness.setToken(UserInfo.accessToken)
//        }
        Wellness.setLocale(Locale.getDefault())
            .initApplication(this@App)
        Wellness.registerTokenExpiredCallback {
//            UserInfo.logout()
        }
    }
}