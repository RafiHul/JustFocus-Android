package com.rafih.justfocus.presentation.ui.screen.focusmode

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.justfocus.domain.model.UiEvent
import com.rafih.justfocus.domain.usecase.UserInstalledAppsUseCase
import com.rafih.justfocus.domain.usecase.blockedapp.FetchBlockedAppUseCase
import com.rafih.justfocus.domain.usecase.blockedapp.InsertBatchBlockedAppUseCase
import com.rafih.justfocus.domain.model.handle.RoomResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FocusModeViewModel @Inject constructor(
    private val insertBatchBlockedAppUseCase: InsertBatchBlockedAppUseCase,
    private val fetchBlockedAppUseCase: FetchBlockedAppUseCase,
    private val userInstalledAppsUseCase: UserInstalledAppsUseCase
): ViewModel(){

    var focusState by mutableStateOf<FocusState>(FocusState.Loading)
        private set

    var showStopWatchDurationPickerDialog by mutableStateOf(false)
        private set

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    private val _selectedAppsPackages = MutableStateFlow<Set<String>>(emptySet())
    val selectedAppsPackages: StateFlow<Set<String>> = _selectedAppsPackages

    private val _allApps = MutableStateFlow<List<ApplicationInfo>>(emptyList())
    val allApps: StateFlow<List<ApplicationInfo>> = _allApps

    //derived state
    val selectedApps: StateFlow<List<ApplicationInfo>> = combine(
        _allApps,
        _selectedAppsPackages
    ) { apps, selectedPackages ->
        apps.filter { it.packageName in selectedPackages }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val unselectedApps = combine(
        _allApps,
        _selectedAppsPackages
    ) { apps, selectedPackages ->
        apps.filter { it.packageName !in selectedPackages }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun loadUserApps(pm: PackageManager){
        viewModelScope.launch {
            focusState = FocusState.Loading

            _allApps.value = userInstalledAppsUseCase.loadInstalledUserApps(pm).filterNotNull()
            val dat = fetchBlockedAppUseCase(pm)
            _selectedAppsPackages.value = dat

            focusState = FocusState.Idle
        }
    }

    fun addSelectedApps(apps: ApplicationInfo){
        _selectedAppsPackages.value += apps.packageName

    }

    fun removeSelectedApps(apps: ApplicationInfo){
        _selectedAppsPackages.value -= apps.packageName
    }

    fun beginToFocusMode(callbackSuccess: () -> Unit) {
        viewModelScope.launch {
            insertBatchBlockedAppUseCase.byPackageName(_selectedAppsPackages.value.toList())
                .collect {
                    when (it) {
                        is RoomResult.Failed -> _uiEvent.emit(UiEvent.ShowToast(it.message))
                        is RoomResult.Success<*> -> {
                            callbackSuccess()
                        }
                    }
                }
        }
    }

    fun showPickerDialog(){
        showStopWatchDurationPickerDialog = true
    }

    fun closePickerDialog(){
        showStopWatchDurationPickerDialog = false
    }

    sealed class FocusState{
        object Loading: FocusState()
        object Idle: FocusState()
    }
}