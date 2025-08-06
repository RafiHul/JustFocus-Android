package com.rafih.justfocus.presentation.ui.screen.appusagestats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.rafih.justfocus.domain.formatDate
import com.rafih.justfocus.domain.formatMillsDurationToString
import com.rafih.justfocus.domain.getDayName
import com.rafih.justfocus.presentation.ui.screen.focusmode.component.TimePickerDialog
import java.time.LocalTime

@Composable
fun AppUsageStatsScreen(
    appPackageName: String,
    modifier: Modifier,
    viewModel: AppUsageStatsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val pm = context.packageManager

    val dateSelected = viewModel.dateSelected.collectAsState()
    val showTimePickerDialog = viewModel.showTimePickerDialog
    val appUsage = viewModel.appUsage


    LaunchedEffect(Unit) {
        viewModel.loadAppUsageStats(appPackageName)
    }

    LaunchedEffect(dateSelected) {
        viewModel.loadAppUsage(appPackageName)
    }

    Column(modifier.padding(horizontal = 16.dp)) {

        when (viewModel.appUsageStatsState) {
            AppUsageStatsViewModel.AppUsageStatsState.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            AppUsageStatsViewModel.AppUsageStatsState.Idle -> {

                dateSelected.value?.let { data ->

                    val appIcon = remember(appPackageName) {
                        pm.getApplicationIcon(appPackageName)
                    }

                    val date = data.date

                    val selectedIndex =  data.listIndex

                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${date.formatDate()} (${date.getDayName()})", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "",
                            Modifier.clickable { viewModel.changeDateSelected(selectedIndex - 1) }
                        )
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = "",
                            Modifier.clickable { viewModel.changeDateSelected(selectedIndex + 1) }
                        )
                    }

                    Card(
                        colors = CardDefaults.cardColors(Color("#F0F2F5".toColorInt())),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth()
                    ) {

                        Row(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Column {
                                Text("Screen Time", fontSize = 16.sp, color = Color.Black)
                                Text(
                                    data.events.sumOf { it.appUsedTimeInMills }.formatMillsDurationToString(),
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(Modifier.weight(1f))

                            AsyncImage(
                                model = appIcon, contentDescription = null, modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                            )

                        }

                    }

                    Text(
                        "Tools",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        appUsage.toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Card(
                        colors = CardDefaults.cardColors(Color("#F0F2F5".toColorInt())),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable{ viewModel.showPickerDialog() }
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text("Set Focus Time", fontSize = 16.sp, color = Color.Black)
                        }
                    }

                    if (appUsage != null){
                        Card(
                            colors = CardDefaults.cardColors(Color("#F0F2F5".toColorInt())),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable{ viewModel.deleteAppUsage(appPackageName) }
                        ) {

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Text("Stop Timer", fontSize = 16.sp, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showTimePickerDialog){
        TimePickerDialog(
            onDismissRequest = { viewModel.closePickerDialog() },
            onConfirmRequest = {
                viewModel.beginTimerApp(it, appPackageName)
                viewModel.closePickerDialog()
            }
        )
    }
}
