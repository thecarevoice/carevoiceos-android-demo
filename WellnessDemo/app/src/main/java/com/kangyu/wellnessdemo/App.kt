package com.kangyu.wellnessdemo

import android.app.Application
import com.carevoice.mindfulnesslibrary.Wellness
import com.kangyu.wellnessdemo.net.CareVoiceOS

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Wellness.setBaseUrl(CareVoiceOS.getBaseUrl()).initApplication(this)
        Wellness.registerTokenExpiredCallback {
            // TODO: clear local auth state and trigger re-login when needed.
        }
    }
}
