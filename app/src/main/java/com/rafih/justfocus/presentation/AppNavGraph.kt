package com.rafih.justfocus.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rafih.justfocus.presentation.ui.screen.appusagestats.AppUsageStatsScreen
import com.rafih.justfocus.presentation.ui.screen.focushistory.FocusHistory
import com.rafih.justfocus.presentation.ui.screen.stopwatch.Stopwatch
import com.rafih.justfocus.presentation.ui.screen.focusmode.FocusModeScreen
import com.rafih.justfocus.presentation.ui.screen.homepage.HomePageScreen
import com.rafih.justfocus.presentation.ui.screen.usagestats.UsageStatsScreen

sealed class AppRoute(val route: String){
    object Home: AppRoute("home_page_screen")
    object UsageStats: AppRoute("usage_stats_screen")
    object FocusMode: AppRoute("focus_mode_screen")
    object FocusHistory: AppRoute("focus_history_screen")
    object StopWatch: AppRoute("stopwatch_screen/{hour}/{minute}"){
        fun createRoute(hour: Int, minute: Int) = "stopwatch_screen/$hour/$minute"
    }
    object AppUsageStats: AppRoute("app_usage_stats_screen/{appPackageName}"){
        fun createRoute(appPackageName: String) = "app_usage_stats_screen/$appPackageName"
    }
}

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Home.route,
        modifier = modifier
    ){
        composable(AppRoute.Home.route) {
            HomePageScreen(
                modifier,
                onNavigateToUsageStatsScreen = {
                    navController.navigate(AppRoute.UsageStats.route)
                },
                onNavigateToFocusMode = {
                    navController.navigate(AppRoute.FocusMode.route)
                },
                onNavigateToFocusHistory = {
                    navController.navigate(AppRoute.FocusHistory.route)
                }
            )
        }

        composable(AppRoute.UsageStats.route) {
            UsageStatsScreen(modifier){
                val nav = AppRoute.AppUsageStats.createRoute(it)
                navController.navigate(nav)
            }
        }

        composable(AppRoute.FocusMode.route) {
            FocusModeScreen(modifier){ hour, minute ->

                val nav = AppRoute.StopWatch.createRoute(hour, minute)
                navController.navigate(nav)

            }
        }

        composable(AppRoute.FocusHistory.route){
            FocusHistory(modifier)
        }

        composable(
            route = AppRoute.StopWatch.route,
            arguments = listOf(
                navArgument("hour") {
                    type = NavType.IntType
                },
                navArgument("minute"){
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val hour = backStackEntry.arguments?.getInt("hour") ?: 0
            val minute = backStackEntry.arguments?.getInt("minute") ?: 0

            Stopwatch(hour, minute, modifier) {
                navController.popBackStack()
            }
        }

        composable(
            route = AppRoute.AppUsageStats.route,
            arguments = listOf(
                navArgument("appPackageName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val appPackageName = backStackEntry.arguments?.getString("appPackageName") ?: ""
            AppUsageStatsScreen(appPackageName, modifier)

        }
    }
}
