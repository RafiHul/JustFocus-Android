package com.rafih.justfocus.presentation.ui.screen.usagestats

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import com.rafih.justfocus.domain.formatMillsDurationToString
import com.rafih.justfocus.domain.getDayName
import com.rafih.justfocus.presentation.ui.screen.usagestats.components.CardItemApp

@Composable
fun UsageStatsScreen(
    modifier: Modifier,
    usageStatsViewModel: UsageStatsViewModel = hiltViewModel(),
    onNavigateToAppUsageStats: (String) -> Unit
) {

    val context = LocalContext.current
    val packageManager = context.packageManager

    val dateSelected = usageStatsViewModel.dateSelected.collectAsState()
    var selectedIndex by remember { mutableIntStateOf(-1) }

    LaunchedEffect(Unit) {
        usageStatsViewModel.loadUsageStatsData(context, packageManager)
    }

    Column(modifier.padding(horizontal = 16.dp)){
        when(usageStatsViewModel.usageStatsState){
            UsageStatsViewModel.UsageStatsState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }
            UsageStatsViewModel.UsageStatsState.Idle -> {

                dateSelected.value?.let { data ->
                    selectedIndex = data.listIndex

                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(data.date.getDayName(), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "", Modifier.clickable{ usageStatsViewModel.changeDateSelected(selectedIndex - 1) })
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = "", Modifier.clickable{ usageStatsViewModel.changeDateSelected(selectedIndex + 1) })

                    }


                    Card(colors = CardDefaults.cardColors(Color("#F0F2F5".toColorInt())), shape = RoundedCornerShape(12.dp), modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()) {

                        Column(Modifier.padding(24.dp)) {
                            Text("Total Usage", fontSize = 16.sp, color = Color.Black)
                            Text(data.events.sumOf { it.appUsedTimeInMills }.formatMillsDurationToString(), color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }

                    }

                    Text("Used Apps", fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

                    LazyColumn(state = rememberLazyListState(), modifier = Modifier.animateContentSize()) {
                        items(items = data.events, key = { it.packageName} ) { items ->
                            if(items.appUsedTimeInMills >= 1000) {
                                val appInfo = packageManager.getApplicationInfo(items.packageName, 5)
                                val totalUsedStringFormat = items.appUsedTimeInMills.formatMillsDurationToString()

                                CardItemApp(appInfo, packageManager, totalUsedStringFormat){
                                    onNavigateToAppUsageStats(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}