package com.rafih.justfocus.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rafih.justfocus.presentation.ui.homepage.HomePageScreen
import com.rafih.justfocus.presentation.ui.usagestats.UsageStatsScreen

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "home_page"
    ){
        composable("home_page") {
            HomePageScreen(
                onNavigateToUsageStatsScreen = {
                    navController.navigate("usage_stats")
                }
            )
        }

        composable("usage_stats") {
            UsageStatsScreen(modifier)
        }
    }
}