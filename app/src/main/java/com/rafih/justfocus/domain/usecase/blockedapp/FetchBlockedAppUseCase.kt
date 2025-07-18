package com.rafih.justfocus.domain.usecase.blockedapp

import android.content.pm.PackageManager
import com.rafih.justfocus.data.repository.BlockedAppRepositoryImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchBlockedAppUseCase @Inject constructor(
    private val blockedAppRepo: BlockedAppRepositoryImpl,
){
    suspend operator fun invoke(pm: PackageManager): Set<String> {
        return blockedAppRepo.fetchBlockedApp(pm)
    }
}