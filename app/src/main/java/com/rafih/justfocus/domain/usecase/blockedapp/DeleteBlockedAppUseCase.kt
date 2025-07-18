package com.rafih.justfocus.domain.usecase.blockedapp

import com.rafih.justfocus.data.repository.BlockedAppRepositoryImpl
import com.rafih.justfocus.data.repository.DataStoreRepositoryImpl
import com.rafih.justfocus.domain.util.RoomResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteBlockedAppUseCase @Inject constructor(
    private val blockedAppRepo: BlockedAppRepositoryImpl
){
    fun all(): Flow<RoomResult> = flow<RoomResult> {
        blockedAppRepo.deleteAllBlockedApp()
        emit(RoomResult.Success(null))
    }.catch {
        emit(RoomResult.Failed( it.message ?: "Terjadi Kesalahan tidak terduga"))
    }

}