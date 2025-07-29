package com.rafih.justfocus.presentation.ui.screen.stopwatch

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.justfocus.domain.model.FocusModeSessionDuration
import com.rafih.justfocus.domain.model.StopwatchDuration
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

    var stopwatchActivity by mutableStateOf("")

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    private var stopwatchStateJob: Job? = null
    private var focusModeSessionDuration by mutableStateOf(FocusModeSessionDuration())

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
            focusModeSessionDuration.startMills = Calendar.getInstance().timeInMillis
            loadStopwatchState() // Reload state setelah service terhubung
        }
    }

    fun setStopWatchDuration(stopWatchDuration: StopwatchDuration){
        viewModelScope.launch {
            if(stopWatchDuration.minute == 0 && stopWatchDuration.hour == 0 && stopWatchDuration.second == 0){
                return@launch
            }

            stopwatchUseCase.setStopWatchDuration(stopWatchDuration)
        }
    }

    fun stopStopwatch(){
        viewModelScope.launch {

            //jadi ini agar akurat dia stop nya pas kapan bukan di lihat dari waktu saat ini, tapi di tambah dari waktu pada timer yang tampil
            val stopTimeMills = focusModeSessionDuration.startMills + stopwatchState.value.toMillis()
            focusModeSessionDuration.stopMills = stopTimeMills

            stopFocusModeUseCase.execute(focusModeSessionDuration, stopwatchActivity).collectLatest {
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

    fun resumeStopwatch(){
        viewModelScope.launch {
            stopwatchUseCase.resumeStopWatch()
        }
    }
}