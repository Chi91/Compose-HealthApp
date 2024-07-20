package com.example.healthapp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.healthapp.ui.screens.share.ShareScreen

@Composable
fun ShareNav(paddingValues: PaddingValues){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination =Screen.ScreenShareRoute.route ){
        composable(route = Screen.ScreenShareRoute.route){
            ShareScreen(paddingValues)
        }
    }
}