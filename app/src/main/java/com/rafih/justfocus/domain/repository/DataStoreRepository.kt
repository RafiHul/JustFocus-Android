package com.rafih.justfocus.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository{
    val focusModeStatus: Flow<Boolean>
    suspend fun setFocusModeEnabled(enabled: Boolean)
}