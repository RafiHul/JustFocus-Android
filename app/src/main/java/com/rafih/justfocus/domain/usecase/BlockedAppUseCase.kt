package com.rafih.justfocus.domain.usecase

import android.content.pm.PackageManager
import android.util.Log
import com.rafih.justfocus.data.repository.BlockedAppRepositoryImpl
import com.rafih.justfocus.data.model.BlockedApp
import com.rafih.justfocus.data.repository.DataStoreRepositoryImpl
import com.rafih.justfocus.domain.util.RoomResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockedAppUseCase @Inject constructor(
    private val blockedAppRepo: BlockedAppRepositoryImpl,
    private val dataStoreRepo: DataStoreRepositoryImpl
) {

    suspend fun addBlockedApp(packageNameList: List<String>): RoomResult{
        try {

            packageNameList.forEach {
                blockedAppRepo.addBlockedApp(BlockedApp(it))
            }

            dataStoreRepo.setFocusModeEnabled(true)
            return RoomResult.Success("Success")

        } catch (e: Exception){
            Log.d("cek err", e.message.toString())
            return RoomResult.Failed
        }
    }

    suspend fun fetchBlockedApp(pm: PackageManager): RoomResult {
        try {
            return RoomResult.Success(blockedAppRepo.fetchBlockedApp(pm))
        } catch (e: Exception){
            Log.d("cek err", e.message.toString())
            return RoomResult.Failed
        }
    }

    suspend fun deleteAllBlockedApp(): RoomResult {
        try {
            blockedAppRepo.deleteAllBlockedApp()
            dataStoreRepo.setFocusModeEnabled(false)
            return RoomResult.Success("Success")
        } catch (e: Exception){
            Log.d("cek err", e.message.toString())
            return RoomResult.Failed
        }
    }
}