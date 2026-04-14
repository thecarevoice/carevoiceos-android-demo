package com.kangyu.wellnessdemo.ui.signup

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
import com.carevoice.mindfulnesslibrary.ui.hubview.DataState
import com.kangyu.wellnessdemo.navigation.CareVoiceScreens
import com.kangyu.wellnessdemo.ui.auth.AuthFooterAction
import com.kangyu.wellnessdemo.ui.auth.AuthPrimaryButton
import com.kangyu.wellnessdemo.ui.auth.AuthScreen
import com.kangyu.wellnessdemo.ui.auth.AuthTextField
import com.kangyu.wellnessdemo.ui.auth.SignupAuthPalette
import com.kangyu.wellnessdemo.ui.signup.vm.SignupViewModel
import com.kangyu.wellnessdemo.ui.theme.WellnessDemoTheme

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
            DataState.Loading -> Unit

            is DataState.Success -> {
                Toast.makeText(
                    ctx,
                    "Registration successful! Please login.",
                    Toast.LENGTH_SHORT
                ).show()
                signupViewModel.resetState()
                navHostController.navigate(CareVoiceScreens.Login.route) {
                    popUpTo(CareVoiceScreens.SignUp.route) {
                        inclusive = true
                    }
                }
            }

            is DataState.Error -> {
                val errorMessage = (state as DataState.Error).message.ifBlank {
                    "Registration failed"
                }
                Toast.makeText(ctx, errorMessage, Toast.LENGTH_LONG).show()
                scaffoldState.snackbarHostState.showSnackbar(errorMessage)
                signupViewModel.resetState()
            }

            DataState.Idle -> Unit
        }
    }

    RegisterPage(
        onClick = { userName, password ->
            signupViewModel.signup(userName, password)
        },
        onBackToLogin = {
            navHostController.navigate(CareVoiceScreens.Login.route) {
                popUpTo(CareVoiceScreens.SignUp.route) { inclusive = true }
            }
        }
    )

    if (state is DataState.Loading) {
        CommonTheme {
            CommonLoadingDialog()
        }
    }
}

@Composable
fun RegisterPage(
    onClick: (String, String) -> Unit,
    onBackToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val passwordsMatch = password == confirmPassword
    val confirmTouched = confirmPassword.isNotBlank()
    val canSubmit = username.isNotBlank() &&
        password.isNotBlank() &&
        confirmPassword.isNotBlank() &&
        passwordsMatch

    AuthScreen(
        palette = SignupAuthPalette,
        eyebrow = "CareVoice OS",
        title = "Create your account",
        formTitle = "Get started",
        formDescription = "Use a work email so your account is ready to sign in across devices."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AuthTextField(
                value = username,
                onValueChange = { username = it },
                label = "Email",
                leadingIcon = Icons.Default.Email,
                accentColor = SignupAuthPalette.accent,
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
                accentColor = SignupAuthPalette.accent,
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
                    imeAction = ImeAction.Next
                )
            )

            AuthTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm password",
                leadingIcon = Icons.Default.Lock,
                accentColor = SignupAuthPalette.accent,
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = if (confirmPasswordVisible) {
                                "Hide password"
                            } else {
                                "Show password"
                            }
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) {
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
                ),
                isError = confirmTouched && !passwordsMatch
            )
        }

        Text(
            text = when {
                !confirmTouched -> "Use at least 8 characters for a stronger password."
                passwordsMatch -> "Passwords match and your account is ready to be created."
                else -> "The passwords do not match yet."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = when {
                !confirmTouched -> SignupAuthPalette.body
                passwordsMatch -> SignupAuthPalette.accentStrong
                else -> MaterialTheme.colorScheme.error
            }
        )

        AuthPrimaryButton(
            text = "Create account",
            accentColor = SignupAuthPalette.accent,
            enabled = canSubmit,
            onClick = { onClick(username.trim(), password) }
        )

        Spacer(modifier = Modifier.height(4.dp))

        AuthFooterAction(
            prompt = "Already registered?",
            actionLabel = "Back to sign in",
            accentColor = SignupAuthPalette.accent,
            onActionClick = onBackToLogin
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterPagePreview() {
    WellnessDemoTheme {
        RegisterPage(
            onClick = { _, _ -> },
            onBackToLogin = {}
        )
    }
}
