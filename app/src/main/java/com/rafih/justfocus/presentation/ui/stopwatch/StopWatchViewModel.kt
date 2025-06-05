package com.rafih.justfocus.presentation.ui.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.justfocus.domain.model.StopwatchState
import com.rafih.justfocus.domain.usecase.BlockedAppUseCase
import com.rafih.justfocus.domain.usecase.StopwatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopWatchViewModel @Inject constructor(
    private val blockedAppUseCase: BlockedAppUseCase,
    private val stopwatchUseCase: StopwatchUseCase
): ViewModel() {

    private val _stopwatchState = MutableStateFlow(StopwatchState(isRunning = false))
    val stopwatchState: StateFlow<StopwatchState> = _stopwatchState

    fun loadStopwatchState(){
        viewModelScope.launch {
            stopwatchUseCase.getStopwatchState()
                .onEach { _stopwatchState.value = it }
                .launchIn(viewModelScope)
        }
    }

    fun startStopwatch(){
        viewModelScope.launch {
            stopwatchUseCase.startStopwatch()
        }
    }

    fun stopStopwatch(){
        stopwatchUseCase.stopStopwatch()
    }

    fun pauseFocusAndStopwatch(){
        viewModelScope.launch {
            blockedAppUseCase.deleteAllBlockedApp()
            stopwatchUseCase.pauseStopwatch()
        }
    }
}