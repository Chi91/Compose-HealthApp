package com.example.healthapp

import android.annotation.SuppressLint
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.healthapp.ui.navigation.BottomMenuItem
import com.example.healthapp.ui.navigation.HealthNavHost

@Composable
fun HealthApp() {
    val navController: NavHostController = rememberNavController()
    Scaffold(
        bottomBar = { HealthBottomAppBar(navController = navController) }
    ) {
        HealthNavHost(
            navController = navController, paddingValues = it
        )
    }
}


@SuppressLint("SuspiciousIndentation")
@Composable
fun HealthBottomAppBar(
    navController: NavController
) {
    val listBottomMenuItems = listOf(
        BottomMenuItem.Home, BottomMenuItem.Info, BottomMenuItem.Share
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar() {
        listBottomMenuItems.forEach {
            val isSelected = it.title == currentDestination?.route
            NavigationBarItem(
                label = { Text(text = it.title) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) {
                            it.iconSelected
                        } else it.iconUnselected, contentDescription = null
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}

