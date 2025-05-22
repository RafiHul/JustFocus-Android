package com.rafih.justfocus.presentation.ui.usagestats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.donutChart.DonutChart
import com.rafih.justfocus.presentation.ui.usagestats.components.CardItemApp

@Composable
fun UsageStatsScreen(
    modifier: Modifier,
    usageStatsViewModel: UsageStatsViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val usageStatsData = usageStatsViewModel.usageStatsData.collectAsState()
    val donutChartData = usageStatsViewModel.donutChartData.collectAsState()
    val packageManager = context.packageManager

    LaunchedEffect(Unit) {
        usageStatsViewModel.loadUsageStatsData(context, packageManager)
    }

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally){
        when(usageStatsViewModel.usageStatsState){
            UsageStatsViewModel.UsageStatsState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }
            UsageStatsViewModel.UsageStatsState.Idle -> {
                usageStatsData.value?.let {

                    DonutChart(pieChartData = donutChartData.value!!, legendPosition = LegendPosition.BOTTOM, modifier = Modifier.size(300.dp))

                    LazyColumn {
                        itemsIndexed(it) { index, items ->
                            val item = items.appUsageEvent
                            if(item.appUsedTimeInMills >= 1000){
                                val otherAppInfo = packageManager.getApplicationInfo(item.packageName, 5)
                                val otherAppIcon = packageManager.getApplicationIcon(otherAppInfo)
                                val otherAppName = packageManager.getApplicationLabel(otherAppInfo).toString()
                                val totalUsed = formatDuration(item.appUsedTimeInMills)

                                CardItemApp(otherAppIcon, otherAppName, totalUsed)
                            }
                        }
                    }
                }
            }
        }


    }
}

fun formatDuration(mills: Long): String {
    val seconds = mills / 1000
    val minutes = seconds / 60
    val remainSeconds = seconds % 60
    val hours = minutes / 60
    val remainMinutes = minutes % 60

    return when {
        hours > 0 -> "$hours jam $remainMinutes menit $seconds detik"
        minutes > 0 -> "$minutes menit $remainSeconds detik"
        else -> "$seconds detik"
    }
}



