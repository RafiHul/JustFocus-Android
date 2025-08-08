package com.rafih.justfocus.presentation

import android.util.Log
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
    object StopWatch: AppRoute("stopwatch_screen/{millis}/{activity}"){
        fun createRoute(millis: Long, activity: String) = "stopwatch_screen/$millis/$activity"
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
                },
                onNavigateToStopWatch = {
                    val nav = AppRoute.StopWatch.createRoute(0L, "hthth")
                    navController.navigate(nav)
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
            FocusModeScreen(modifier){ millis, activity ->

                val nav = AppRoute.StopWatch.createRoute(millis, activity)
                navController.navigate(nav)
            }
        }

        composable(AppRoute.FocusHistory.route){
            FocusHistory(modifier)
        }

        composable(
            route = AppRoute.StopWatch.route,
            arguments = listOf(
                navArgument("millis") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val millis = backStackEntry.arguments?.getLong("millis") ?: 0L
            val activity = backStackEntry.arguments?.getString("activity") ?: "Kerja"

            Log.d("cek second tumbal", millis.toString()) //ini aneh dia harus di log agar tidak 0 dan sesuai value di dalam stopwatch ????

            Stopwatch(millis, activity, modifier, navigateToFocuMode = {
                navController.popBackStack()
            }, navigateToHomePage = {
                navController.navigate(AppRoute.Home.route)
            })
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
