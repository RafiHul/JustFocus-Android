package com.rafih.justfocus.presentation.ui.screen.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.justfocus.domain.model.StopWatchDuration
import com.rafih.justfocus.domain.model.StopwatchState
import com.rafih.justfocus.domain.model.UiEvent
import com.rafih.justfocus.domain.usecase.stopwatch.StopwatchUseCase
import com.rafih.justfocus.domain.usecase.stopwatch.StartStopwatchUseCase
import com.rafih.justfocus.domain.usecase.stopwatch.StopFocusModeUseCase
import com.rafih.justfocus.domain.handleUiEvent
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
    private val stopwatchUseCase: StopwatchUseCase,
    private val stopFocusModeUseCase: StopFocusModeUseCase
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

    fun startStopwatch(activity: String){
        viewModelScope.launch {
            startStopwatchUseCase(activity)
            loadStopwatchState() // Reload state setelah service terhubung
        }
    }

    fun setStopWatchDuration(stopWatchDuration: StopWatchDuration){
        viewModelScope.launch {
            if(stopWatchDuration.minute == 0 && stopWatchDuration.hour == 0 && stopWatchDuration.second == 0){
                return@launch
            }

            stopwatchUseCase.setStopWatchDuration(stopWatchDuration)
        }
    }

    fun stopStopwatch(){
        viewModelScope.launch {
            stopFocusModeUseCase.execute().collectLatest {
                it.handleUiEvent(_uiEvent){
                    _stopwatchState.value = StopwatchState(isRunning = false)
                }
            }
        }
    }

    // TODO: ini resume jadi pr
    fun pauseStopwatch(){
        viewModelScope.launch {
            stopwatchUseCase.pauseStopwatch()
        }
    }

    fun resumeStopwatch(){
        viewModelScope.launch {
            stopwatchUseCase.resumeStopWatch()
        }
    }
}