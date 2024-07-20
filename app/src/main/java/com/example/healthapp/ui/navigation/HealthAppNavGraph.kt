package com.example.healthapp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun HealthNavHost(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = BottomMenuItem.Home.route
    ) {
        composable(route= BottomMenuItem.Home.route){
            HomeNav(paddingValues)
        }
        composable(route= BottomMenuItem.Info.route){
            InfoNav(paddingValues)
        }
        composable(route= BottomMenuItem.Share.route){
            ShareNav(paddingValues)
        }
    }
}





