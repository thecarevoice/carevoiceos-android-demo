package com.kangyu.wellnessdemo.ui.login.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carevoice.mindfulnesslibrary.WellnessSDK
import com.carevoice.mindfulnesslibrary.ui.hubview.DataState
import com.kangyu.wellnessdemo.net.NetUtils
import com.kangyu.wellnessdemo.net.bean.LoginRequest
import com.kangyu.wellnessdemo.net.bean.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel:ViewModel() {

    private val _loginState = MutableStateFlow<DataState<LoginResponse>?>(null)
     val loginState = _loginState.asStateFlow()

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

    fun resetLoginState(){
        _loginState.value = null
    }

}