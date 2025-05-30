package com.rafih.justfocus.data.repository

import com.rafih.justfocus.data.local.datastore.DataStoreManager
import com.rafih.justfocus.domain.repository.DataStoreRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
): DataStoreRepository {

    override val focusModeStatus = dataStoreManager.focusModeStatus

    override suspend fun setFocusModeEnabled(enabled: Boolean){
        dataStoreManager.setFocusModeEnabled(enabled)
    }

}