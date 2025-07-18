package com.rafih.justfocus.presentation.ui.screen.appusagestats

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rafih.justfocus.domain.model.AppUsageGroup
import com.rafih.justfocus.domain.usecase.UsageStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AppUsageStatsViewModel @Inject constructor(
    private val usageStatsUSeCase: UsageStatsUseCase
): ViewModel() {

    var appUsageStatsState by mutableStateOf<AppUsageStatsState>(AppUsageStatsState.Loading)
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

    sealed class AppUsageStatsState{
        object Idle: AppUsageStatsState()
        object Loading: AppUsageStatsState()
    }
}