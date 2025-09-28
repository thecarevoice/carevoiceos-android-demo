package com.kangyu.wellnessdemo.ui.login

import android.util.Log
import android.widget.Toast
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
import com.carevoice.cvdesign.designsystem.CommonLoadingDialog
import com.carevoice.cvdesign.designsystem.theme.CommonTheme
import com.carevoice.mindfulnesslibrary.WellnessSDK
import com.carevoice.mindfulnesslibrary.WellnessTool
import com.carevoice.mindfulnesslibrary.ui.hubview.DataState
import com.kangyu.wellnessdemo.navigation.CareVoiceScreens
import com.kangyu.wellnessdemo.net.CareVoiceOS
import com.kangyu.wellnessdemo.net.NetUtils
import com.kangyu.wellnessdemo.ui.login.vm.LoginViewModel
import com.kangyu.wellnessdemo.ui.signup.vm.SignupViewModel
import com.kangyu.wellnessdemo.utils.JwtUtils

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
                
                // 打印完整的data.sdk信息用于调试
                Log.d("LoginRoute", "Login Success - SDK Data:")
                Log.d("LoginRoute", "  accessToken: ${data.sdk.accessToken}")
                Log.d("LoginRoute", "  refreshToken: ${data.sdk.refreshToken}")
                Log.d("LoginRoute", "  expiresIn: ${data.sdk.expiresIn}")
                
                // retrieve tenant code from access token
                val tenantCode = JwtUtils.getTenantCodeFromJwt(data.sdk.accessToken)
                Log.d("LoginRoute", "  parsed tenantCode: $tenantCode")
                
                // 也打印JWT解析的完整内容
                val jwtObj = JwtUtils.parseJwt(data.sdk.accessToken)
                Log.d("LoginRoute", "  JWT payload: $jwtObj")
                
                if (tenantCode != null) {
                    // 设置CareVoice SDK的生产环境URL
                    val careVoiceBaseUrl = "https://apis.carevoiceos.com"
                    Log.d("LoginRoute", "Initializing WellnessSDK with:")
                    Log.d("LoginRoute", "  baseUrl: $careVoiceBaseUrl")
                    Log.d("LoginRoute", "  tenantCode: $tenantCode")
                    Log.d("LoginRoute", "  expiresIn: ${data.sdk.expiresIn}")
                    // 根据登录跳转规范：登录成功后跳转至MainHome页面，并清除登录页栈记录

                    WellnessSDK.setBaseUrl(careVoiceBaseUrl).setToken(data.sdk.accessToken)
                        .setRefreshToken(data.sdk.refreshToken)
                        .setExpiresIn(data.sdk.expiresIn.toLong())
                        .setTenantCode(tenantCode).init(ctx) {
                            Log.d("LoginRoute", "WellnessSDK init success")
                    }

                    navHostController.navigate(CareVoiceScreens.Main.route) {
                        // 清除登录页面的历史记录，防止用户返回
                        popUpTo(CareVoiceScreens.Login.route) { inclusive = true }
                    }
                } else {
                    Toast.makeText(ctx, "Failed to get tenant code from token", Toast.LENGTH_SHORT).show()
                    Log.e("LoginRoute", "Failed to parse tenant code from JWT token")
                    Log.e("LoginRoute", "JWT token: ${data.sdk.accessToken}")
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
fun LoginPage(gotoRegister: () -> Unit, onClick: (String, String) -> Unit, gotoMain: () -> Unit) {
    var username by remember { mutableStateOf("mingxiang.luo@thecarevoice.com") }
    var password by remember { mutableStateOf("12345678") }
    var passwordVisible by remember { mutableStateOf(false) }

    // 渐变背景
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF6366F1), // 紫色
            Color(0xFF8B5CF6), // 紫罗兰色
            Color(0xFFA855F7)  // 品红色
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
                // 可以在这里添加Logo图片
                Text(
                    text = "CareVoice",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Welcome Back",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // 登录卡片
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
                        text = "Login",
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
                                tint = Color(0xFF6366F1)
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF6366F1),
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
                                tint = Color(0xFF6366F1)
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
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF6366F1),
                            unfocusedBorderColor = Color(0xFFE5E7EB),
                            backgroundColor = Color(0xFFF9FAFB)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 登录按钮
                    Button(
                        onClick = { onClick.invoke(username, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF6366F1)
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 12.dp
                        )
                    ) {
                        Text(
                            text = "Login",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 注册链接
                    Text(
                        text = "Don't have an account? Sign up",
                        modifier = Modifier.clickable { gotoRegister() },
                        color = Color(0xFF6366F1),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


//Preview
@Preview
@Composable
fun LoginPagePreview() {
    LoginPage(gotoRegister = {}, onClick = { _, _ -> },{

    })
}