package com.kangyu.wellnessdemo.ui.signup.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carevoice.mindfulnesslibrary.ui.hubview.DataState
import com.kangyu.wellnessdemo.net.NetUtils
import com.kangyu.wellnessdemo.net.bean.LoginRequest
import com.kangyu.wellnessdemo.net.bean.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignupViewModel:ViewModel() {

    private val _signupState = MutableStateFlow<DataState<LoginResponse>>(DataState.Loading)
     val signupState = _signupState.asStateFlow()

    fun signup(username: String, password: String) {

       val apiService =  NetUtils.getApiService()
        viewModelScope.launch {
            try {
                val response = apiService.register(LoginRequest(username, password))
                if (response.success && response.data != null) {
                    _signupState.value = DataState.Success(response.data!!)
                }else{
                    _signupState.value = DataState.Error(response.message)
                }
            }catch (e:Exception){
                _signupState.value = DataState.Error(e.message?:"")
                e.printStackTrace()
            }
        }
    }

    fun resetState(){
        _signupState.value = DataState.Loading
    }

}