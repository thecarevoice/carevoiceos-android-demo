package com.kangyu.wellnessdemo.ui.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kangyu.wellnessdemo.ui.login.LoginPage
import com.kangyu.wellnessdemo.ui.signup.RegisterPage

/**
 * UI Preview file to showcase optimized login and registration pages in English
 */

@Preview(showBackground = true, name = "Optimized Login Page (English)")
@Composable
fun OptimizedLoginPreview() {
    LoginPage(
        gotoRegister = { },
        onClick = { _, _ -> },
        gotoMain = { }
    )
}

@Preview(showBackground = true, name = "Optimized Registration Page (English)")
@Composable
fun OptimizedRegisterPreview() {
    RegisterPage(
        onClick = { _, _ -> },
        onBackToLogin = { }
    )
}

@Preview(showBackground = true, name = "Login and Registration Comparison (English)", heightDp = 1200)
@Composable
fun LoginRegisterComparisonPreview() {
    Column {
        // Login page
        LoginPage(
            gotoRegister = { },
            onClick = { _, _ -> },
            gotoMain = { }
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Registration page
        RegisterPage(
            onClick = { _, _ -> },
            onBackToLogin = { }
        )
    }
}