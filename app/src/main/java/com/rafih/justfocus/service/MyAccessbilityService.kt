package com.rafih.justfocus.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import com.rafih.justfocus.data.repository.BlockedAppRepositoryImpl
import com.rafih.justfocus.data.repository.DataStoreRepositoryImpl
import com.rafih.justfocus.di.ServiceEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyAccessbilityService: AccessibilityService() {

    private lateinit var blockedAppRepository: BlockedAppRepositoryImpl
    private lateinit var dataStoreRepository: DataStoreRepositoryImpl

    var isFocus = false

    override fun onServiceConnected() {
        super.onServiceConnected()

        val entryPoint = EntryPointAccessors.fromApplication(
            this,
            ServiceEntryPoint::class.java
        )

        blockedAppRepository = entryPoint.provideBlockedAppRepository()
        dataStoreRepository = entryPoint.provideDataStoreRepository()

        CoroutineScope(Dispatchers.IO).launch {
            dataStoreRepository.focusModeStatus.collect {
                isFocus = it
            }
            blockedAppRepository.loadBlockedApp()
        }

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            val packageName = event.packageName?.toString() ?: return
            if(::blockedAppRepository.isInitialized && blockedAppRepository.isAppBlocked(packageName) && isFocus){

                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(applicationContext, "Blokir $packageName", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onInterrupt() {
    }
}