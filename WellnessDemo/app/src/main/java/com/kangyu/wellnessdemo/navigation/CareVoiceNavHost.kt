package com.kangyu.wellnessdemo.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.carevoice.cvandroid.navigation.AppComposeNavigator
import com.carevoice.lib_common.util.findContextActivity
import com.carevoice.mindfulnesslibrary.navigation.articlesListNavigation
import com.carevoice.mindfulnesslibrary.navigation.vm.SharedViewModel
import com.carevoice.mindfulnesslibrary.navigation.wellnessNavigation
import com.kangyu.wellnessdemo.ui.login.LoginRoute
import com.kangyu.wellnessdemo.ui.signup.SignupRoute

@Composable
fun CareVoiceNavHost(
    navHostController: NavHostController,
    composeNavigator: AppComposeNavigator,
    scaffoldState: ScaffoldState
) {

    NavHost(
        navController = navHostController,
        startDestination = CareVoiceScreens.Login.route,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {

        composable(
            route = CareVoiceScreens.SignUp.route
        ) {
            SignupRoute(navHostController, composeNavigator, scaffoldState)
        }


        composable(
            route = CareVoiceScreens.Login.route
        ) {
            LoginRoute(navHostController, composeNavigator, scaffoldState)
        }

    }
}