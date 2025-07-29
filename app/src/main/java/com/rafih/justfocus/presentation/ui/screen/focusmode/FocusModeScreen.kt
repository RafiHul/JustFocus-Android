package com.rafih.justfocus.presentation.ui.screen.focusmode

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rafih.justfocus.domain.model.UiEvent
import com.rafih.justfocus.presentation.ui.screen.focusmode.component.CardItemApp
import com.rafih.justfocus.presentation.ui.screen.focusmode.component.DropDownActivity
import com.rafih.justfocus.presentation.ui.screen.focusmode.component.StopwatchDurationPickerDialog
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalTime

@Composable
fun FocusModeScreen(
    modifier: Modifier,
    focusModeViewModel: FocusModeViewModel = hiltViewModel(),
    onNavigateToStopWatch: (Int, Int, Int, String) -> Unit
) {
    val context = LocalContext.current
    val pm = context.packageManager

    val selectedApps = focusModeViewModel.selectedApps.collectAsState()
    val unselectedApps = focusModeViewModel.unselectedApps.collectAsState()
    val showStopWatchDurationPickerDialog = focusModeViewModel.showStopWatchDurationPickerDialog
    val activitySelected = focusModeViewModel.activitySelected

    var stopwatchDuration by remember { mutableStateOf<LocalTime>(LocalTime.of(0, 0, 0)) }

    LaunchedEffect(Unit) {
        focusModeViewModel.uiEvent.collectLatest {
            when(it){
                is UiEvent.ShowToast -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        focusModeViewModel.loadUserApps(pm)
    }

    Column(modifier.padding(horizontal = 16.dp)) {
        when(focusModeViewModel.focusState){
            FocusModeViewModel.FocusState.Idle -> {

                Button(onClick = {
                    focusModeViewModel.beginToFocusMode {
                        stopwatchDuration.let { onNavigateToStopWatch(it.hour, it.minute, it.second, activitySelected) }
                    }
                }) {
                    Text("Focus")
                }

                Button(onClick = { focusModeViewModel.showPickerDialog() }) {
                    Text("Pick Time")
                }

                DropDownActivity(activitySelected){
                    focusModeViewModel.activitySelected = it
                }

                Text("Apps selected")
                LazyColumn(state = rememberLazyListState(), modifier = Modifier.animateContentSize()) {
                    items(items = selectedApps.value, key = { it.packageName }) { app ->

                        CardItemApp(app = app, pm = pm, selected = true){
                            focusModeViewModel.removeSelectedApps(app)
                        }
                    }
                }

                Text("Select more apps")
                LazyColumn(state = rememberLazyListState(), modifier = Modifier.animateContentSize()) {
                    items(items = unselectedApps.value, key = { it.packageName }) { app ->

                        CardItemApp(app = app, pm = pm, selected = false){
                            focusModeViewModel.addSelectedApps(app)
                        }
                    }
                }


            }
            FocusModeViewModel.FocusState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }
        }
    }

    if (showStopWatchDurationPickerDialog){
        StopwatchDurationPickerDialog(
            onDismissRequest = { focusModeViewModel.closePickerDialog() },
            onConfirmRequest = { time ->
                stopwatchDuration = time
                focusModeViewModel.closePickerDialog()
            }
        )
    }
}