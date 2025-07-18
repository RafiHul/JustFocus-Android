package com.rafih.justfocus.presentation.ui.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.justfocus.domain.model.StopwatchDuration
import com.rafih.justfocus.domain.model.StopwatchState
import com.rafih.justfocus.domain.model.UiEvent
import com.rafih.justfocus.domain.usecase.stopwatch.StopwatchUseCase
import com.rafih.justfocus.domain.usecase.stopwatch.StartStopwatchUseCase
import com.rafih.justfocus.handleUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopWatchViewModel @Inject constructor(
    private val startStopwatchUseCase: StartStopwatchUseCase,
    private val stopwatchUseCase: StopwatchUseCase
): ViewModel() {

    private val _stopwatchState = MutableStateFlow(StopwatchState(isRunning = false))
    val stopwatchState: StateFlow<StopwatchState> = _stopwatchState

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    private var stopwatchStateJob: Job? = null

    fun loadStopwatchState(){
        stopwatchStateJob?.cancel()
        stopwatchStateJob = viewModelScope.launch {
            stopwatchUseCase.getStopwatchState().collect {
                _stopwatchState.value = it
            }
        }
    }

    fun startStopwatch(){
        viewModelScope.launch {
            startStopwatchUseCase()
            loadStopwatchState() // Reload state setelah service terhubung
        }
    }

    fun setStopWatchDuration(stopWatchDuration: StopwatchDuration){
        viewModelScope.launch {
            if(stopWatchDuration.minute == 0 && stopWatchDuration.hour == 0){
                return@launch
            }

            stopwatchUseCase.setStopWatchDuration(stopWatchDuration)
        }
    }

    fun stopStopwatch(){
        viewModelScope.launch {
            stopwatchUseCase.stopStopwatch().collectLatest {
                it.handleUiEvent(_uiEvent){
                    _stopwatchState.value = StopwatchState(isRunning = false)
                }
            }
        }
    }

    fun pauseStopwatch(){
        viewModelScope.launch {
            stopwatchUseCase.pauseStopwatch()
        }
    }
}