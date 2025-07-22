package com.rafih.justfocus.presentation.ui.screen.focushistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafih.justfocus.domain.model.FocusHistoryData
import com.rafih.justfocus.domain.usecase.focushistory.FetchFocusHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FocusHistoryViewModel @Inject constructor(
    private val fetchFocusHistoryUseCase: FetchFocusHistoryUseCase
): ViewModel(){
    private val _focusHistoryData: MutableStateFlow<List<FocusHistoryData>> = MutableStateFlow(emptyList())
    val focusHistoryData: StateFlow<List<FocusHistoryData>> = _focusHistoryData

    fun fetchFocusHistoryData(){
        viewModelScope.launch {
            fetchFocusHistoryUseCase().collect {
                _focusHistoryData.value = it
            }
        }
    }
}