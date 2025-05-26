package com.rafih.justfocus.data.repository

import com.rafih.justfocus.data.local.datastore.DataStoreManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {

    val focusModeStatus = dataStoreManager.focusModeStatus

    suspend fun setFocusModeEnabled(enabled: Boolean){
        dataStoreManager.setFocusModeEnabled(enabled)
    }

}