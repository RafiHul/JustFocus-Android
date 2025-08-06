package com.rafih.justfocus.presentation.ui.screen.appusagestats

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.justfocus.data.model.AppUsageLimit
import com.rafih.justfocus.domain.handleUiEvent
import com.rafih.justfocus.domain.model.AppUsageGroup
import com.rafih.justfocus.domain.model.UiEvent
import com.rafih.justfocus.domain.timeToMills
import com.rafih.justfocus.domain.usecase.AppUsageLimitUseCase
import com.rafih.justfocus.domain.usecase.UsageStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AppUsageStatsViewModel @Inject constructor(
    private val usageStatsUSeCase: UsageStatsUseCase,
    private val appUsageLimitUseCase: AppUsageLimitUseCase
): ViewModel() {

    var appUsageStatsState by mutableStateOf<AppUsageStatsState>(AppUsageStatsState.Loading)
        private set

    var showTimePickerDialog by mutableStateOf(false)
        private set

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    var appUsage by mutableStateOf<Long?>(null)
        private set

    private val _dateSelected = MutableStateFlow<AppUsageGroup?>(null)
    val dateSelected: StateFlow<AppUsageGroup?> = _dateSelected

    private val _weeklyAppUsageStatsData = MutableStateFlow<List<AppUsageGroup>>(emptyList())
    val weeklyAppUsageStatsData: StateFlow<List<AppUsageGroup>> = _weeklyAppUsageStatsData

    fun loadAppUsageStats(appPackageName: String){
        val res = usageStatsUSeCase.getCachedWeeklyUsage().map { AppUsageGroup(it.date,it.events.filter { it.packageName == appPackageName },it.listIndex) }
        _weeklyAppUsageStatsData.value = res
        _dateSelected.value = res.find { it.date.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) }

        appUsageStatsState = AppUsageStatsState.Idle
    }

    fun changeDateSelected(idx: Int){
        if(idx !in 0.._weeklyAppUsageStatsData.value.size - 1){
            return
        }
        _dateSelected.value = _weeklyAppUsageStatsData.value[idx]
    }

    fun loadAppUsage(packageName: String){
        viewModelScope.launch {
            appUsage = appUsageLimitUseCase.load(packageName)?.usageMillis
        }
    }

    fun deleteAppUsage(packageName: String){
        viewModelScope.launch {
            appUsageLimitUseCase.delete(packageName)
        }
    }

    fun beginTimerApp(time: LocalTime, packageName: String){
        viewModelScope.launch {
            val appUsageLimit = AppUsageLimit(packageName, time.timeToMills())
            Log.d("cek bug timer limit 0", appUsageLimit.toString())
            appUsageLimitUseCase.add(appUsageLimit).collect {
                it.handleUiEvent(_uiEvent){}
            }
        }

    }

    fun showPickerDialog(){
        showTimePickerDialog = true
    }

    fun closePickerDialog(){
        showTimePickerDialog = false
    }

    sealed class AppUsageStatsState{
        object Idle: AppUsageStatsState()
        object Loading: AppUsageStatsState()
    }
}