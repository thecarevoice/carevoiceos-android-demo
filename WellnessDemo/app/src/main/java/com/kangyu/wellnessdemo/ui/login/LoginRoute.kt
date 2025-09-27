package com.kangyu.wellnessdemo.ui.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.carevoice.cvandroid.navigation.AppComposeNavigator
import com.carevoice.cvdesign.designsystem.CommonLoadingDialog
import com.carevoice.cvdesign.designsystem.theme.CommonTheme
import com.carevoice.mindfulnesslibrary.WellnessSDK
import com.carevoice.mindfulnesslibrary.WellnessTool
import com.carevoice.mindfulnesslibrary.ui.hubview.DataState
import com.kangyu.wellnessdemo.navigation.CareVoiceScreens
import com.kangyu.wellnessdemo.net.NetUtils
import com.kangyu.wellnessdemo.ui.login.vm.LoginViewModel
import com.kangyu.wellnessdemo.ui.signup.vm.SignupViewModel

@Composable
fun LoginRoute(
    navHostController: NavHostController,
    composeNavigator: AppComposeNavigator,
    scaffoldState: ScaffoldState
) {

    val viewModel = remember { LoginViewModel() }
    val state by viewModel.loginState.collectAsState()
    val ctx = LocalContext.current
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(state) {
        when (state) {
            DataState.Loading -> {
                // Handle loading state
            }

            is DataState.Success -> {
                // Handle success state
                val data = (state as DataState.Success).data
                WellnessSDK.setBaseUrl(NetUtils.getBaseUrl()).setToken(data.sdk.accessToken)
                    .setRefreshToken(data.sdk.refreshToken)
                    .setExpiresIn(data.sdk.expiresIn.toLong()).setTenantCode("C7T1JxIX").init(ctx) {
                    WellnessTool.startHubViewActivity(ctx)
                }
//                viewModel.resetLoginState()
            }

            is DataState.Error -> {
                // Handle error state
                Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show()
                scaffoldState.snackbarHostState.showSnackbar(errorMessage)
                viewModel.resetLoginState()
            }
            else->{

            }
        }
    }

    LoginPage(gotoRegister = {
        navHostController.navigate(CareVoiceScreens.SignUp.route)
    }, onClick = { username, password ->
        viewModel.login(username, password)
    }, gotoMain = {
        navHostController.navigate(CareVoiceScreens.Main.route)
    })

    if (state is DataState.Loading) CommonTheme{ CommonLoadingDialog()}
}

@Composable
fun LoginPage(gotoRegister: () -> Unit, onClick: (String, String) -> Unit,gotoMain: () -> Unit) {
    var username by remember { mutableStateOf("mingxiang.luo@thecarevoice.com") }
    var password by remember { mutableStateOf("12345678") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onClick.invoke(username, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { gotoMain.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Goto Main")
        }

        Text(
            text = "Don't have an account? Register",
            modifier = Modifier.clickable { gotoRegister() },
            style = MaterialTheme.typography.body2
        )
    }
}


//Preview
@Preview
@Composable
fun LoginPagePreview() {
    LoginPage(gotoRegister = {}, onClick = { _, _ -> },{

    })
}