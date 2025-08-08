package com.rafih.justfocus.presentation.ui.screen.homepage

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.justfocus.data.model.BlockedShort
import com.rafih.justfocus.data.repository.BlockedShortRepositoryImpl
import com.rafih.justfocus.data.repository.DataStoreRepositoryImpl
import com.rafih.justfocus.service.MyAccessbilityService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val blockedShortRepository: BlockedShortRepositoryImpl,
    private val dataStoreRepository: DataStoreRepositoryImpl
): ViewModel() {

    val blockedShortItem = listOf(
        MyAccessbilityService.INSTAGRAM_PACKAGE_NAME,
        MyAccessbilityService.WHATSAPP_PACKAGE_NAME,
        MyAccessbilityService.YOUTUBE_PACKAGE_NAME
    )

    var selectedBlockedShort by mutableStateOf<List<String>>(emptyList())
        private set

    var switchState by mutableStateOf(false)
        private set

    // TODO: Kalo accessbility nya mati dia tidak work & cek di db ketika apk keluar
    fun initSwitchState(){
        val blockedShort = blockedShortRepository.chachedBlockedShortPackageName

        if(blockedShort.isNotEmpty()){
            switchState = true
            selectedBlockedShort = blockedShort
        }
    }

    fun switchStateChange(newState: Boolean){
        switchState = newState
        viewModelScope.launch {
            if(switchState){
                blockedShortRepository.deleteAllBlockedShort()
                blockedShortRepository.addBatchBlockedShort(selectedBlockedShort.map { BlockedShort(it) })
            } else {
                blockedShortRepository.deleteAllBlockedShort()
                blockedShortRepository.loadBlockedShort()
            }
        }
    }

    fun toggleSelection(item: String) {
        selectedBlockedShort = if (selectedBlockedShort.contains(item)) {
            selectedBlockedShort - item
        } else {
            selectedBlockedShort + item
        }

        if(switchState){ //jika false jangan di eksekusi
            //matikan ketika memilih yang baru
            switchStateChange(false)
        }
    }

    fun toggleFocusModeMenu(
        navigateToFocusMode: () -> Unit,
        navigateToStopWatch: () -> Unit
    ){
        viewModelScope.launch {
            dataStoreRepository.focusModeStatus.collectLatest {
                if (it) navigateToStopWatch() else navigateToFocusMode()
            }
        }
    }
}