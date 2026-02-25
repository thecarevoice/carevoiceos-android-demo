package com.kangyu.wellnessdemo.ui.signup

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.carevoice.cvandroid.navigation.AppComposeNavigator
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
    
    // Show loading dialog when in loading state
    if (state is DataState.Loading) {
        CommonTheme { 
            CommonLoadingDialog()
        }
    }
}

@Composable
fun RegisterPage(onClick: (String, String) -> Unit, onBackToLogin: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // 渐变背景（绿色系）
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF10B981), // 绿色
            Color(0xFF059669), // 深绿色
            Color(0xFF047857)  // 更深的绿色
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo/品牌区域
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text(
                    text = "CareVoice",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Create Your Account",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // 注册卡片
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp)),
                elevation = 16.dp,
                backgroundColor = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign Up",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // 用户名输入框
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Email Address", color = Color(0xFF6B7280)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = Color(0xFF10B981)
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color(0xFFE5E7EB),
                            backgroundColor = Color(0xFFF9FAFB)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 密码输入框
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", color = Color(0xFF6B7280)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password",
                                tint = Color(0xFF10B981)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                    tint = Color(0xFF6B7280)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color(0xFFE5E7EB),
                            backgroundColor = Color(0xFFF9FAFB)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 确认密码输入框
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password", color = Color(0xFF6B7280)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Confirm Password",
                                tint = Color(0xFF10B981)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                                    tint = Color(0xFF6B7280)
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF10B981),
                            unfocusedBorderColor = Color(0xFFE5E7EB),
                            backgroundColor = Color(0xFFF9FAFB)
                        )
                    )

                    // 密码匹配提示
                    if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
                        Text(
                            text = "Passwords do not match",
                            color = Color(0xFFEF4444),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 注册按钮
                    Button(
                        onClick = { 
                            if (password == confirmPassword) {
                                onClick.invoke(username, password)
                            }
                        },
                        enabled = username.isNotEmpty() && password.isNotEmpty() && password == confirmPassword,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF10B981),
                            disabledBackgroundColor = Color(0xFFE5E7EB)
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 12.dp
                        )
                    ) {
                        Text(
                            text = "Sign Up",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 登录链接
                    Text(
                        text = "Already have an account? Sign in",
                        modifier = Modifier.clickable { onBackToLogin() },
                        color = Color(0xFF10B981),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// Preview
@Preview
@Composable
fun RegisterPagePreview() {
    RegisterPage(
        onClick = { _, _ -> },
        onBackToLogin = { }
    )
}