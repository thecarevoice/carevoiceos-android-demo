package com.kangyu.wellnessdemo.ui.login.vm

import DeepLinkBean
import DeepLinkBeanResponse
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carevoice.cvandroid.utils.ifNull
import com.carevoice.mindfulnesslibrary.WellnessSDK
import com.carevoice.mindfulnesslibrary.ui.hubview.DataState
import com.carevoice.mindfulnesslibrary.uistate.CommonUiState
import com.kangyu.wellnessdemo.net.NetUtils
import com.kangyu.wellnessdemo.net.bean.DeepLinkRequest
import com.kangyu.wellnessdemo.net.bean.DeepLinkResponse
import com.kangyu.wellnessdemo.net.bean.LoginRequest
import com.kangyu.wellnessdemo.net.bean.LoginResponse
import com.kangyu.wellnessdemo.utils.PkceUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel:ViewModel() {

    private val _loginState = MutableStateFlow<DataState<LoginResponse>?>(null)
     val loginState = _loginState.asStateFlow()

    var codeVerifier by mutableStateOf<String?>(null)
        private set

    private val _deepLinkState = MutableStateFlow<DataState<DeepLinkBeanResponse>?>(null)
    val deepLinkState = _deepLinkState.asStateFlow()

    private val _deepLinkValidateState = MutableStateFlow<DataState<DeepLinkResponse>?>(null)
    val deepLinkValidateState = _deepLinkValidateState.asStateFlow()

    init {
        codeVerifier = PkceUtil.generateCodeVerifier()
    }
    fun login(username: String, password: String) {
       val apiService =  NetUtils.getApiService()
        viewModelScope.launch {
            try {
                _loginState.value = DataState.Loading
                val response = apiService.login(LoginRequest(username, password))
                if (response.success && response.data != null) {
                    _loginState.value = DataState.Success(response.data!!)
                }else{
                    _loginState.value = DataState.Error(response.message)
                }
            }catch (e:Exception){
                _loginState.value = DataState.Error(e.message?:"")
                e.printStackTrace()
            }
        }
    }

    fun generatePkcePair(): String {
//        codeVerifier = PkceUtil.generateCodeVerifier()
        return PkceUtil.generateCodeChallenge(codeVerifier!!)
    }


    fun deepLink(codeChallenge: String,codeChallengeMethod: String, cvUserUniqueId: String) {
        val apiService =  NetUtils.getApiService()
        viewModelScope.launch {
            try {
                _deepLinkState.value = DataState.Loading
                val response = apiService.deepLink(DeepLinkBean(codeChallenge, codeChallengeMethod,cvUserUniqueId))
                if (response.success && response.data != null) {
                    _deepLinkState.value = DataState.Success(response.data!!)
                }else{
                    _deepLinkState.value = DataState.Error(response.message)
                }
            }catch (e:Exception){
                _deepLinkState.value = DataState.Error(e.message?:"")
                e.printStackTrace()
            }
        }
    }


    fun validateDeepLink(deepLinkRequest: DeepLinkRequest) {
        val apiService =  NetUtils.getApiService()

        viewModelScope.launch {
            try {
                _deepLinkValidateState.value = DataState.Loading
                val response = apiService.validateDeepLink(deepLinkRequest)
                if (response.success && response.data != null) {
                    _deepLinkValidateState.value = DataState.Success(response.data!!)
                }else{
                    _deepLinkValidateState.value = DataState.Error(response.message)
                }
            }catch (e:Exception){
                _deepLinkValidateState.value = DataState.Error(e.message?:"")
                e.printStackTrace()
            }
        }
    }

    fun resetLoginState(){
        _loginState.value = null
    }

}