package com.kangyu.wellnessdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.carevoice.cvandroid.navigation.AppComposeNavigator
import com.carevoice.cvandroid.navigation.CogniFitComposeNavigator
import com.carevoice.mindfulnesslibrary.WellnessTool
import com.kangyu.wellnessdemo.navigation.CareVoiceNavHost
import com.kangyu.wellnessdemo.ui.theme.WellnessDemoTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val appComposeNavigator: AppComposeNavigator = CogniFitComposeNavigator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()
            val scaffoldState = rememberScaffoldState()

            WellnessDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost = {
                }) { innerPadding ->
                    innerPadding
                    Column(
                        modifier = Modifier
//                            .padding(innerPadding)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CareVoiceNavHost(navHostController, appComposeNavigator, scaffoldState)
                    }
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WellnessDemoTheme {
        Greeting("Android")
    }
}