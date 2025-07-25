package com.rafih.justfocus.domain.usecase

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.rafih.justfocus.data.repository.UserApplicationRepositoryImpl
import com.rafih.justfocus.domain.repository.UserApplicationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInstalledAppsUseCase @Inject constructor(
    val repo: UserApplicationRepositoryImpl
){
    suspend fun loadInstalledUserApps(pm: PackageManager): List<ApplicationInfo?>{
        return repo.fetchAppInstalled(pm)
    }
}