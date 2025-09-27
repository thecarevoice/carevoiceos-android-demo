package com.kangyu.wellnessdemo.ui.signup

import android.util.Log
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
import com.carevoice.mindfulnesslibrary.WellnessSDK
import com.carevoice.mindfulnesslibrary.WellnessTool
import com.carevoice.mindfulnesslibrary.ui.hubview.DataState
import com.kangyu.wellnessdemo.navigation.CareVoiceScreens
import com.kangyu.wellnessdemo.net.NetUtils
import com.kangyu.wellnessdemo.ui.signup.vm.SignupViewModel
import android.widget.Toast
import com.carevoice.cvdesign.designsystem.CommonLoadingDialog
import com.carevoice.cvdesign.designsystem.theme.CommonTheme

@Composable
fun SignupRoute(
    navHostController: NavHostController,
    composeNavigator: AppComposeNavigator,
    scaffoldState: ScaffoldState
) {
    val signupViewModel = remember { SignupViewModel() }
    val state by signupViewModel.signupState.collectAsState()
    val ctx = LocalContext.current

    LaunchedEffect(state) {
        when (state) {
            DataState.Loading -> {
                // Handle loading state
            }
            
            is DataState.Success -> {
                // Handle success state - show success message and navigate to login
                Toast.makeText(ctx, "Registration successful! Please login.", Toast.LENGTH_SHORT).show()
                
                // Reset the signup state to avoid repeated navigation
                signupViewModel.resetState()
                
                // Navigate to login screen
                navHostController.navigate(CareVoiceScreens.Login.route) {
                    // Clear the signup screen from back stack
                    popUpTo(CareVoiceScreens.SignUp.route) {
                        inclusive = true
                    }
                }
            }

            is DataState.Error -> {
                // Handle error state
                val errorMessage = (state as DataState.Error).message
                Toast.makeText(ctx, "Registration failed: $errorMessage", Toast.LENGTH_LONG).show()
                signupViewModel.resetState()
            }
            
            DataState.Idle -> {
                // Idle state - do nothing
            }
        }
    }
    
    RegisterPage(onClick = { userName, password ->
        signupViewModel.signup(userName, password)
    })
    
    // Show loading dialog when in loading state
    if (state is DataState.Loading) {
        CommonTheme { 
            CommonLoadingDialog()
        }
    }
}

@Composable
fun RegisterPage(onClick: (String, String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp)
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
            Text(text = "Register")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Already have an account? Login",
            modifier = Modifier.clickable { /* Navigate to login page */ },
            style = MaterialTheme.typography.body2
        )
    }
}

// Preview
@Preview
@Composable
fun RegisterPagePreview() {
    RegisterPage(onClick = { _, _ -> })
}