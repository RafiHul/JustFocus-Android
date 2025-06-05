package com.rafih.justfocus.presentation.ui.stopwatch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun Stopwatch(
    hour: Int, minute: Int,
    modifier: Modifier,
    viewModel: StopWatchViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {

    val time = viewModel.stopwatchState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStopwatchState()
        viewModel.startStopwatch()
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(time.value.let { String.format("%02d:%02d:%02d", it.hours, it.minutes, it.seconds) })

        Button(onClick = {
//            viewModel.stopFocusAndStopwatch()
            viewModel.stopStopwatch()
            navigateBack()
        }) {
            Text("Stop Focus")
        }
    }
}