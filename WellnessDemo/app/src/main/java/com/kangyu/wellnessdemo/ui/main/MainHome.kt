package com.kangyu.wellnessdemo.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.carevoice.cvandroid.navigation.AppComposeNavigator
import com.carevoice.cvdesign.designsystem.modifier.containWindowInseTop
import com.carevoice.cvdesign.designsystem.theme.CommonTheme
import com.carevoice.mindfulnesslibrary.WellnessTool
//import com.carevoice.mindfulnesslibrary.navigation.WellnessAreaScreens
//import com.carevoice.mindfulnesslibrary.navigation.WellnessHubScreens
import kotlinx.coroutines.launch


@Composable
fun MainHome(
    navHostController: NavHostController,
    composeNavigator: AppComposeNavigator,
    scaffoldState: ScaffoldState
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(0, pageCount = { 3 })
    Column(modifier = Modifier.fillMaxSize().containWindowInseTop(false)) {
        Box(modifier = Modifier.weight(1f)) {

            HorizontalPager(pagerState) {

                when (it) {
                    0 -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text("123123123123123")
                            Box(
                                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                            ) {
                                Column {
                                    Text("your home page", modifier = Modifier.clickable {

                                    })
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Button(onClick = {
//                                        WellnessTool.startHubViewActivity(this@MainActivity)
                                    }) {
                                        Text("Click Enter Wellness SDK")
                                    }
                                }

                            }
                        }

                    }

                    1 -> {
//                        WellnessHubScreens(composeNavigator = composeNavigator)
                    }

                    2 -> {
//                        WellnessAreaScreens(composeNavigator = composeNavigator)
                    }
                }
            }
        }
        Row(modifier = Modifier.padding(0.dp, 24.dp)) {
            Box(modifier = Modifier
                .weight(1f)
                .clickable {
                    scope.launch {
                        pagerState.scrollToPage(0)
                    }
                }, contentAlignment = Alignment.Center) {
                androidx.compose.material.Text("Home")
            }

            Box(modifier = Modifier
                .weight(1f)
                .clickable {
                    scope.launch {
                        pagerState.scrollToPage(1)
                    }
                }, contentAlignment = Alignment.Center) {
                androidx.compose.material.Text("Wellness")
            }
            Box(modifier = Modifier
                .weight(1f)
                .clickable {
                    scope.launch {
                        pagerState.scrollToPage(2)
                    }
                }, contentAlignment = Alignment.Center) {
                androidx.compose.material.Text("Profile")
            }


        }
    }
}