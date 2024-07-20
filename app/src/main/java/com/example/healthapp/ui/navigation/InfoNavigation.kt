package com.example.healthapp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.healthapp.ui.screens.info.InfoTopicScreen
import com.example.healthapp.ui.screens.info.InfoDrugDetailScreen
import com.example.healthapp.ui.screens.info.InfoScreen

@Composable
fun InfoNav(paddingValues: PaddingValues) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.ScreenInfoRoute.route) {
        composable(route = Screen.ScreenInfoRoute.route) {
            InfoScreen(
                navigateToDrugDetail = { navController.navigate(route = "${Screen.ScreenInfoDrugDetail.route}/$it") },
                navigateToTopic = { navController.navigate(route = Screen.ScreenInfoTopic.route) },
                modifier = Modifier.padding(paddingValues)
            )
        }
        composable(
            route = "${Screen.ScreenInfoDrugDetail.route}/{pzn}",
            arguments = listOf(navArgument("pzn") {
                type = NavType.StringType
            })
        ) {
            InfoDrugDetailScreen(
                modifier = Modifier.padding(paddingValues),
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.ScreenInfoTopic.route
        ) {
            InfoTopicScreen(
                modifier = Modifier.padding(paddingValues),
                navigateBack = { navController.popBackStack() })
        }
    }
}

