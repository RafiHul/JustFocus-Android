package com.rafih.justfocus.presentation.ui.screen.stopwatch

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rafih.justfocus.domain.model.StopwatchDuration
import com.rafih.justfocus.domain.model.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun Stopwatch(
    hour: Int, minute: Int,
    modifier: Modifier,
    viewModel: StopWatchViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {

    val time = viewModel.stopwatchState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadStopwatchState()
        viewModel.setStopWatchDuration(StopwatchDuration(hour, minute))
        viewModel.startStopwatch()

        viewModel.uiEvent.collectLatest {
            when(it){
                is UiEvent.ShowToast -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(time.value.let { String.format("%02d:%02d:%02d", it.hours, it.minutes, it.seconds) })

        Button(onClick = {
            viewModel.stopStopwatch()
            navigateBack()
        }) {
            Text("Stop Focus")
        }

        Button(onClick = {
            viewModel.pauseStopwatch()
        }) {
            Text("Pause Focus")
        }
    }
}