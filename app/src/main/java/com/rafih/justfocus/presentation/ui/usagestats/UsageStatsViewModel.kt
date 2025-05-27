package com.rafih.justfocus.presentation.ui.usagestats

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.justfocus.domain.model.AppUsageGroup
import com.rafih.justfocus.domain.usecase.UsageStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class UsageStatsViewModel @Inject constructor(
    private val usageStatsUseCase: UsageStatsUseCase
): ViewModel() {

    var usageStatsState by mutableStateOf<UsageStatsState>(UsageStatsState.Loading)
        private set

    private val _dateSelected = MutableStateFlow<AppUsageGroup?>(null)
    val dateSelected: StateFlow<AppUsageGroup?> = _dateSelected

    private val _weeklyUsageStatsData = MutableStateFlow<MutableList<AppUsageGroup>>(mutableListOf())
    val weeklyUsageStatsData: StateFlow<MutableList<AppUsageGroup>> = _weeklyUsageStatsData

    fun loadUsageStatsData(context: Context, pm: PackageManager){
        viewModelScope.launch {
            usageStatsState = UsageStatsState.Loading
            val result = usageStatsUseCase.getWeeklyUsage(context, pm)
            _weeklyUsageStatsData.value = result
            _dateSelected.value = result.find { it.date.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) }  //get today data
            usageStatsState = UsageStatsState.Idle
        }
    }

    fun changeDateSelected(idx: Int){
        if(idx !in 0..weeklyUsageStatsData.value.size - 1){
            return
        }
        _dateSelected.value = weeklyUsageStatsData.value[idx]
    }

    sealed class UsageStatsState{
        object Loading: UsageStatsState()
        object Idle: UsageStatsState()
    }
}