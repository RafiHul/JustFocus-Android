package com.rafih.justfocus.presentation.ui.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.justfocus.domain.model.StopwatchDuration
import com.rafih.justfocus.domain.usecase.BlockedAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopWatchViewModel @Inject constructor(
    private val blockedAppUseCase: BlockedAppUseCase
): ViewModel() {


    var second = MutableStateFlow(0)
        private set

    var minute = MutableStateFlow(0)
        private set

    var hour = MutableStateFlow(0)
        private set

    var isRunning = MutableStateFlow(true)
        private set

    private val _stopwatchDuration = MutableStateFlow<StopwatchDuration?>(null)
    val stopwatchDuration: Flow<StopwatchDuration?> = _stopwatchDuration

    val counter = combine(
        second, minute, hour
    ) { sec, min, hour ->
        String.format("%02d:%02d:%02d",hour, min, sec)
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), "00:00"
    )

    fun startStopwatch(){
        viewModelScope.launch {

            while(isRunning.value) {
                delay(1000)

                val nSecond = second.value + 1
                if(nSecond == 60){
                    second.value = 0
                    minute.value += 1

                    if(minute.value == 60){
                        minute.value = 0
                        hour.value += 1
                    }

                } else {
                    second.value = nSecond
                }

                _stopwatchDuration.value?.let {
                    if (it.hour == hour.value && it.minute == minute.value) {
                        isRunning.value = false
                    }
                }

            }
        }
    }

    fun setStopwatchDuration(stopwatchDuration: StopwatchDuration) {
        _stopwatchDuration.value = stopwatchDuration
    }

    fun stopFocusAndStopwatch(){
        viewModelScope.launch {
            blockedAppUseCase.deleteAllBlockedApp()
            isRunning.value = false
        }
    }
}