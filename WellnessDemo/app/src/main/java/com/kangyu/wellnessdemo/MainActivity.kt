package com.kangyu.wellnessdemo

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.carevoice.mindfulnesslibrary.MindfulnessConfig
import com.carevoice.mindfulnesslibrary.MindfulnessSDK
import com.kangyu.wellnessdemo.ui.theme.WellnessDemoTheme
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.TimeZone

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WellnessDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Click enter Wellness SDK")

                        Button(onClick = {
                            val header = hashMapOf<String, String>().apply {
                                put("Locale", Locale.getDefault().country)
                                put("Time-Zone", TimeZone.getDefault().id)
                                put("Agent", "Android")
                                put("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ijc5Mzg3MjNmNiJ9.eyJhdWQiOiI0YWEyMmRmYi1lNmQ3LTRiODQtOWYyNy1mNGYxMjljNmEyZmYiLCJleHAiOjE3MzkwODY4OTMsImlhdCI6MTczOTAwMDQ5MywiaXNzIjoiYWNtZS5jb20iLCJzdWIiOiIwNjk1OTlmNC0xMjIxLTRmNmItOGYyNS0yMjdmYTdjZWJhNTciLCJqdGkiOiI5Y2U0OGNlMy1hZWRlLTRjMDctYTBlYy02YmI1N2YzNTAxOGMiLCJhdXRoZW50aWNhdGlvblR5cGUiOiJQQVNTV09SRCIsImVtYWlsIjoiZGVtbzA5MjAxQDEyNi5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXBwbGljYXRpb25JZCI6IjRhYTIyZGZiLWU2ZDctNGI4NC05ZjI3LWY0ZjEyOWM2YTJmZiIsInJvbGVzIjpbXSwiYXV0aF90aW1lIjoxNzM5MDAwNDkzLCJ0aWQiOiI2ZmJiNGJlMS1lY2Y4LTRjZjAtOWFlMi03ZGI4ODQyNjIwOGEifQ.UO9T_g1Nmp2g8zJhYEUXCdAWXb5KVYj8xqrJD2RjyfY")
                                put("Version", "0.5.0")
                                put("Accept-Encoding", "gzip")
                                put("Lang", "en")
                            }
                            lifecycleScope.launch {
                                MindfulnessSDK.init(this@MainActivity, "https://p2-stag.kangyu.info/", header, )
                                MindfulnessConfig.startHubViewActivity(this@MainActivity)
                            }
                        }) { Text("Enter Wellness SDK") }

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