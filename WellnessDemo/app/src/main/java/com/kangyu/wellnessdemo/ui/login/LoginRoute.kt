package com.kangyu.wellnessdemo.ui.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.carevoice.cvandroid.navigation.AppComposeNavigator
import com.carevoice.cvdesign.designsystem.CommonLoadingDialog
import com.carevoice.cvdesign.designsystem.theme.CommonTheme
import com.carevoice.mindfulnesslibrary.Wellness
import com.carevoice.mindfulnesslibrary.ui.hubview.DataState
import com.kangyu.wellnessdemo.navigation.CareVoiceScreens
import com.kangyu.wellnessdemo.net.CareVoiceOS
import com.kangyu.wellnessdemo.ui.auth.AuthFooterAction
import com.kangyu.wellnessdemo.ui.auth.AuthPrimaryButton
import com.kangyu.wellnessdemo.ui.auth.AuthScreen
import com.kangyu.wellnessdemo.ui.auth.AuthTextField
import com.kangyu.wellnessdemo.ui.auth.LoginAuthPalette
import com.kangyu.wellnessdemo.ui.login.vm.LoginViewModel
import com.kangyu.wellnessdemo.ui.theme.WellnessDemoTheme
import com.kangyu.wellnessdemo.utils.JwtUtils
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun LoginRoute(
    navHostController: NavHostController,
    composeNavigator: AppComposeNavigator,
    scaffoldState: ScaffoldState
) {
    val viewModel = remember { LoginViewModel() }
    val state by viewModel.loginState.collectAsState()
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(state) {
        when (state) {
            DataState.Loading -> Unit

            is DataState.Success -> {
                val data = (state as DataState.Success).data
                Log.d("LoginRoute", "Login Success - SDK Data:")
                Log.d("LoginRoute", "  accessToken: ${data.sdk.accessToken}")
                Log.d("LoginRoute", "  refreshToken: ${data.sdk.refreshToken}")
                Log.d("LoginRoute", "  expiresIn: ${data.sdk.expiresIn}")

                val tenantCode = JwtUtils.getTenantCodeFromJwt(data.sdk.accessToken)
                Log.d("LoginRoute", "  parsed tenantCode: $tenantCode")
                Log.d("LoginRoute", "  JWT payload: ${JwtUtils.parseJwt(data.sdk.accessToken)}")

                if (tenantCode != null) {
                    val careVoiceBaseUrl = CareVoiceOS.getBaseUrl()
                    Log.d("LoginRoute", "Initializing WellnessSDK with:")
                    Log.d("LoginRoute", "  baseUrl: $careVoiceBaseUrl")
                    Log.d("LoginRoute", "  tenantCode: $tenantCode")
                    Log.d("LoginRoute", "  expiresIn: ${data.sdk.expiresIn}")

                    scope.launch {
                        initWellnessSDK(
                            token = data.sdk.accessToken,
                            expiresIn = data.sdk.expiresIn,
                            refreshToken = data.sdk.refreshToken,
                            tenantCode = tenantCode
                        )
                        navHostController.navigate(CareVoiceScreens.Main.route) {
                            popUpTo(CareVoiceScreens.Login.route) { inclusive = true }
                        }
                    }
                } else {
                    Toast.makeText(
                        ctx,
                        "Failed to get tenant code from token",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("LoginRoute", "Failed to parse tenant code from JWT token")
                    Log.e("LoginRoute", "JWT token: ${data.sdk.accessToken}")
                }
            }

            is DataState.Error -> {
                val message = (state as DataState.Error).message.ifBlank { "Login failed" }
                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
                scaffoldState.snackbarHostState.showSnackbar(message)
                viewModel.resetLoginState()
            }

            else -> Unit
        }
    }

    LoginPage(
        gotoRegister = {
            navHostController.navigate(CareVoiceScreens.SignUp.route)
        },
        onClick = { username, password ->
            viewModel.login(username, password)
        }
    )

    if (state is DataState.Loading || state is DataState.Success) {
        CommonTheme { CommonLoadingDialog() }
    }
}

private suspend fun initWellnessSDK(
    token: String,
    expiresIn: Long,
    refreshToken: String,
    tenantCode: String
) {
    Wellness.setToken(token)
        .setTenantCode(tenantCode)
        .setExpiresIn(expiresIn)
        .setRefreshToken(refreshToken)
        .setLocale(Locale.getDefault())
    Wellness.init()
}

@Composable
fun LoginPage(
    gotoRegister: () -> Unit,
    onClick: (String, String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val canSubmit = username.isNotBlank() && password.isNotBlank()

    AuthScreen(
        palette = LoginAuthPalette,
        eyebrow = "CareVoice OS",
        title = "Welcome back",
        formTitle = "Sign in",
        formDescription = "Use the email linked to your workspace to continue."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AuthTextField(
                value = username,
                onValueChange = { username = it },
                label = "Email",
                leadingIcon = Icons.Default.Email,
                accentColor = LoginAuthPalette.accent,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = Icons.Default.Lock,
                accentColor = LoginAuthPalette.accent,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = if (passwordVisible) {
                                "Hide password"
                            } else {
                                "Show password"
                            }
                        )
                    }
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (canSubmit) onClick(username.trim(), password)
                    }
                )
            )
        }

        Text(
            text = "Your session is protected and kept lightweight for faster entry.",
            style = MaterialTheme.typography.bodyMedium,
            color = LoginAuthPalette.body
        )

        AuthPrimaryButton(
            text = "Sign in",
            accentColor = LoginAuthPalette.accent,
            enabled = canSubmit,
            onClick = { onClick(username.trim(), password) }
        )

        Spacer(modifier = Modifier.height(4.dp))

        AuthFooterAction(
            prompt = "New to CareVoice?",
            actionLabel = "Create an account",
            accentColor = LoginAuthPalette.accent,
            onActionClick = gotoRegister
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPagePreview() {
    WellnessDemoTheme {
        LoginPage(
            gotoRegister = {},
            onClick = { _, _ -> }
        )
    }
}
