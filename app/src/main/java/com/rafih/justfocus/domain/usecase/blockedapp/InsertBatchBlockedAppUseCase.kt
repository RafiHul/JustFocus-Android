package com.rafih.justfocus.domain.usecase.blockedapp

import android.util.Log
import com.rafih.justfocus.data.model.BlockedApp
import com.rafih.justfocus.data.repository.BlockedAppRepositoryImpl
import com.rafih.justfocus.data.repository.DataStoreRepositoryImpl
import com.rafih.justfocus.domain.util.RoomResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertBatchBlockedAppUseCase @Inject constructor(
    private val blockedAppRepo: BlockedAppRepositoryImpl,
    private val dataStoreRepo: DataStoreRepositoryImpl
) {
    fun byPackageName(packageNameList: List<String>): Flow<RoomResult> = flow<RoomResult> {
        val blockedAppList = packageNameList.map { BlockedApp(it) }

        blockedAppRepo.addBatchBlockedApp(blockedAppList)
        dataStoreRepo.setFocusModeStatus(true)

        emit(RoomResult.Success(null))
    }.catch {
        emit(RoomResult.Failed( it.message ?: "Terjadi Kesalahan tidak terduga"))
    }
}