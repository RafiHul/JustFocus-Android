package com.rafih.justfocus.presentation.ui.usagestats

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.donutChart.DonutChart
import com.aay.compose.donutChart.model.PieChartData
import com.rafih.justfocus.R
import com.rafih.justfocus.presentation.ui.usagestats.components.CardItemApp
import java.util.Calendar

@Composable
fun UsageStatsScreen(
    modifier: Modifier,
    usageStatsViewModel: UsageStatsViewModel = viewModel()
) {

    val context = LocalContext.current
    val usageStatsData = usageStatsViewModel.usageStatsData.collectAsState()
    val packageManager = context.packageManager

    LaunchedEffect(Unit) {
        usageStatsViewModel.loadUsageStatsData(context, packageManager)
    }

    val donutChartData: List<PieChartData> = listOf(
        PieChartData(20.0, Color.Red, "mobile entod"),
        PieChartData(40.0, Color.Blue, "menkrep"),
        PieChartData(30.0, Color.Green, "fesnuk"),
        PieChartData(15.0, Color.Cyan, "ituu dia"),
        PieChartData(70.0, Color.Magenta, "mboh wes"),
    )

    Column(modifier) {
        when(usageStatsViewModel.usageStatsState){
            UsageStatsViewModel.UsageStatsState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }
            UsageStatsViewModel.UsageStatsState.Idle -> {
                usageStatsData.value?.let {

                    DonutChart(pieChartData = donutChartData, centerTitle = "penggunaan", legendPosition = LegendPosition.BOTTOM, modifier = Modifier.size(100.dp))

                    LazyColumn {
                        itemsIndexed(it) { index, item ->
                            if(item.totalTimeInForeground >= 1000){
                                val otherAppInfo = packageManager.getApplicationInfo(item.packageName, 5)
                                val otherAppIcon = packageManager.getApplicationIcon(otherAppInfo)
                                val otherAppName = packageManager.getApplicationLabel(otherAppInfo).toString()
                                val totalUsed = formatDuration(item.totalTimeInForeground)

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



