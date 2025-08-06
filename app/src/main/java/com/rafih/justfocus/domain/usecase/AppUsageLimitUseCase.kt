package com.rafih.justfocus.domain.usecase

import com.rafih.justfocus.data.model.AppUsageLimit
import com.rafih.justfocus.data.repository.AppUsageLimitRepositoryImpl
import com.rafih.justfocus.domain.model.handle.RoomResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUsageLimitUseCase @Inject constructor(
    private val appUsageLimitRepository: AppUsageLimitRepositoryImpl
){
    fun add(appUsageLimit: AppUsageLimit): Flow<RoomResult> = flow<RoomResult> {
        appUsageLimitRepository.add(appUsageLimit)
        emit(RoomResult.Success(null))
    }.catch {
        emit(RoomResult.Failed( it.message ?: "Terjadi Kesalahan tidak terduga"))
    }

    suspend fun load(packageName: String): AppUsageLimit? {
        appUsageLimitRepository.load() // TODO: ini load nyapindahin ketika buka aplikasi aja biar gk terus terusan load
        return appUsageLimitRepository.cachedAppHasUsageLimit.find { it.packageName == packageName }
    }

    suspend fun delete(packageName: String){
        appUsageLimitRepository.deleteByPackageName(packageName)
    }
}