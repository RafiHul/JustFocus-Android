package com.rafih.justfocus

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import com.rafih.justfocus.data.local.repository.BlockedAppRepository
import com.rafih.justfocus.di.ServiceEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyAccessbilityService: AccessibilityService() {

    private lateinit var blockedAppRepository: BlockedAppRepository

    override fun onServiceConnected() {
        super.onServiceConnected()

        val dao = EntryPointAccessors.fromApplication(
            this,
            ServiceEntryPoint::class.java
        ).provideBlockedAppDaoForService()

        blockedAppRepository = BlockedAppRepository(dao)

        CoroutineScope(Dispatchers.IO).launch {
            blockedAppRepository.loadBlockedApps()
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            val packageName = event.packageName?.toString() ?: return

            if(::blockedAppRepository.isInitialized && blockedAppRepository.isAppBlocked(packageName)){

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