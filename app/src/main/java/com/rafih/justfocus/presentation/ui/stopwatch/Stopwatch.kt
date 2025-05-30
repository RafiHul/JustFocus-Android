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
import com.rafih.justfocus.domain.model.StopwatchDuration

@Composable
fun Stopwatch(
    hour: Int, minute: Int,
    modifier: Modifier,
    viewModel: StopWatchViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {

    val time = viewModel.counter.collectAsState()

    LaunchedEffect(Unit) {

        if(hour != 0 || minute != 0){
            viewModel.setStopwatchDuration(StopwatchDuration(hour, minute))
        }

        viewModel.startStopwatch()
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(time.value.toString())

        Button(onClick = {
            viewModel.stopFocusAndStopwatch()
            navigateBack()
        }) {
            Text("Stop Focus")
        }
    }
}