package com.rafih.justfocus.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rafih.justfocus.presentation.ui.stopwatch.Stopwatch
import com.rafih.justfocus.presentation.ui.focusmode.FocusModeScreen
import com.rafih.justfocus.presentation.ui.homepage.HomePageScreen
import com.rafih.justfocus.presentation.ui.usagestats.UsageStatsScreen

sealed class AppRoute(val route: String){
    object Home: AppRoute("home_page_screen")
    object UsageStast: AppRoute("usage_stats_screen")
    object FocusMode: AppRoute("focus_mode_screen")
    object StopWatch: AppRoute("stopwatch_screen/{hour}/{minute}"){
        fun createRoute(hour: Int, minute: Int) = "stopwatch_screen/$hour/$minute"
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
                    navController.navigate(AppRoute.UsageStast.route)
                },
                onNavigateToFocusMode = {
                    navController.navigate(AppRoute.FocusMode.route)
                }
            )
        }

        composable(AppRoute.UsageStast.route) {
            UsageStatsScreen(modifier)
        }

        composable(AppRoute.FocusMode.route) {
            FocusModeScreen(modifier){ hour, minute ->
                Log.d("cek ini", minute.toString())
                val nav = AppRoute.StopWatch.createRoute(hour, minute)
                navController.navigate(nav)
            }
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
    }
}
