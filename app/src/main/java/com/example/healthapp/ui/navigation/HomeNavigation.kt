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
import com.example.healthapp.ui.screens.home.HomeScreen
import com.example.healthapp.ui.screens.home.detail.HomeDetailScreen
import com.example.healthapp.ui.screens.home.detail.HomeEditScreen
import com.example.healthapp.ui.screens.home.detail.add.AddingScreen
import com.example.healthapp.ui.screens.home.detail.add.OptionScreen

@Composable
fun HomeNav(paddingValues: PaddingValues) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.ScreenHomeRoute.route) {
        composable(route = Screen.ScreenHomeRoute.route) {
            HomeScreen(
                modifier = Modifier.padding(paddingValues),
                navigateToItem = { navController.navigate(route = "${Screen.ScreenHomeDetailRoute.route}/$it") },
                onNextDetailScreen = { navController.navigate(route = Screen.ScreenHomeAddOptionRoute.route) })
        }
        composable(
            route = "${Screen.ScreenHomeDetailRoute.route}/{pzn}",
            arguments = listOf(navArgument("pzn") {
                type = NavType.StringType
            })
        ) {
            HomeDetailScreen(
                navigateHome = {
                    navController.popBackStack(
                        route = Screen.ScreenHomeRoute.route, inclusive = false
                    )
                },
                navigateBack = {
                    navController.popBackStack(
                        route = Screen.ScreenHomeRoute.route, inclusive = false
                    )
                },
                navigateTo = { navController.navigate(route = "${Screen.ScreenHomeEditRoute.route}/$it") },
                modifier = Modifier.padding(paddingValues)
            )
        }
        composable(route = Screen.ScreenHomeAddOptionRoute.route) {
            OptionScreen(
                navigateBack = { navController.navigateUp() },
                modifier = Modifier.padding(paddingValues),
                onNextScreen = { navController.navigate(route = "${Screen.ScreenHomeSaveRoute.route}/$it") },
            )
        }
        composable(
            route = "${Screen.ScreenHomeEditRoute.route}/{pzn}",
            arguments = listOf(navArgument("pzn") {
                type = NavType.StringType
            })
        ) {
            HomeEditScreen(
                modifier = Modifier.padding(paddingValues),
                navigateBack = { navController.popBackStack() },
                navigateHome = {
                    navController.popBackStack(
                        route = Screen.ScreenHomeRoute.route, inclusive = false
                    )
                })
        }
        composable(
            route = "${Screen.ScreenHomeSaveRoute.route}/{pzn}",
            arguments = listOf(navArgument("pzn") {
                type = NavType.StringType
            })
        ) {
            AddingScreen(
                navigateBack = { navController.popBackStack() },
                navigateHome = {
                    navController.popBackStack(
                        route = Screen.ScreenHomeRoute.route, inclusive = false
                    )
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}
