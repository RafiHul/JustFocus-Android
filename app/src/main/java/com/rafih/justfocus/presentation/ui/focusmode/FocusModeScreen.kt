package com.rafih.justfocus.presentation.ui.focusmode

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rafih.justfocus.presentation.ui.focusmode.component.CardItemApp

@Composable
fun FocusModeScreen(
    modifier: Modifier,
    focusModeViewModel: FocusModeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val pm = context.packageManager

    val selectedApps = focusModeViewModel.selectedApps.collectAsState()
    val unselectedApps = focusModeViewModel.unselectedApps.collectAsState()

    LaunchedEffect(Unit) {
        focusModeViewModel.loadUserApps(pm)
    }

    Column(modifier.padding(horizontal = 16.dp)) {
        when(focusModeViewModel.focusState){
            FocusModeViewModel.FocusState.Idle -> {

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
}