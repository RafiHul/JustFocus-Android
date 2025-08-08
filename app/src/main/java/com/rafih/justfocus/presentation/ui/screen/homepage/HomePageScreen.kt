package com.rafih.justfocus.presentation.ui.screen.homepage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.collections.listOf

@Composable
fun HomePageScreen(
    modifier: Modifier,
    onNavigateToUsageStatsScreen: () -> Unit,
    onNavigateToFocusMode: () -> Unit,
    onNavigateToFocusHistory: () -> Unit,
    onNavigateToStopWatch: () -> Unit,
    viewModel: HomePageViewModel = hiltViewModel()
){

    val switchState = viewModel.switchState

    LaunchedEffect(Unit) {
        viewModel.initSwitchState()
    }

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxSize()) {
        Button(onClick = { viewModel.toggleFocusModeMenu(onNavigateToFocusMode, onNavigateToStopWatch) }) {
            Text("Focuss Mode")
        }
        Button(onClick = onNavigateToUsageStatsScreen) {
            Text("Usage Stats Screen")
        }
        Button(onClick = onNavigateToFocusHistory) {
            Text("Focus History")
        }
        CheckBoxSelectionAntiShort(viewModel)

        Row {
            Text("Anti Reels/Short/Story")
            Switch(
                checked = switchState,
                onCheckedChange = {
                    viewModel.switchStateChange(it)
                }
            )
        }
    }
}

@Composable
fun CheckBoxSelectionAntiShort(viewModel: HomePageViewModel) {
    val radioOptions = listOf("Instagram", "Whatsapp", "Youtube")
    val selectedBlockedShort = viewModel.selectedBlockedShort

    Column {
        radioOptions.forEachIndexed { idx, text ->
            val fullPackageName = viewModel.blockedShortItem[idx]
            val isSelected = selectedBlockedShort.contains(fullPackageName)

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable {
                        viewModel.toggleSelection(fullPackageName)
                    }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = {
                        viewModel.toggleSelection(fullPackageName)
                    }
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}