package com.rafih.justfocus.presentation.ui.screen.homepage

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.justfocus.data.model.BlockedShort
import com.rafih.justfocus.data.repository.BlockedShortRepositoryImpl
import com.rafih.justfocus.service.MyAccessbilityService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val blockedShortRepository: BlockedShortRepositoryImpl
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

    // TODO: Kalo accessbility nya mati dia tidak work
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
                Log.d("cek", "sucess block app")
            } else {
                blockedShortRepository.deleteAllBlockedShort()
                blockedShortRepository.loadBlockedShort()
                Log.d("cek", "sucess delete")
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
}