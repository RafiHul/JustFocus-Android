package com.rafih.justfocus.domain.usecase

import android.util.Log
import com.rafih.justfocus.data.repository.BlockedAppRepository
import com.rafih.justfocus.data.model.BlockedApp
import com.rafih.justfocus.domain.util.RoomResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockedAppUseCase @Inject constructor(
    private val repo: BlockedAppRepository
) {

    suspend fun addBlockedApp(packageNameList: MutableList<String>): RoomResult{
        try {

            packageNameList.forEach {
                repo.addBlockedApp(BlockedApp(it))
            }
            return RoomResult.Success("Success")

        } catch (e: Exception){
            Log.d("cek err", e.message.toString())
            return RoomResult.Failed
        }
    }
}