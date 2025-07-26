package com.rafih.justfocus.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.rafih.justfocus.data.repository.BlockedAppRepositoryImpl
import com.rafih.justfocus.data.repository.BlockedShortRepositoryImpl
import com.rafih.justfocus.data.repository.DataStoreRepositoryImpl
import com.rafih.justfocus.di.ServiceEntryPoint
import com.rafih.justfocus.domain.formatViewIdToPackageName
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyAccessbilityService: AccessibilityService() {

    private lateinit var blockedAppRepository: BlockedAppRepositoryImpl
    private lateinit var blockedShortRepository: BlockedShortRepositoryImpl
    private lateinit var dataStoreRepository: DataStoreRepositoryImpl
    var isFocus = false

    override fun onServiceConnected() {
        super.onServiceConnected()

        val entryPoint = EntryPointAccessors.fromApplication(
            this,
            ServiceEntryPoint::class.java
        )

        blockedAppRepository = entryPoint.provideBlockedAppRepository()
        blockedShortRepository = entryPoint.provideBlockedShortRepository()
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
                intentToAndroidHome()
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(applicationContext, "Blokir $packageName", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
            val rootNode: AccessibilityNodeInfo? = rootInActiveWindow

            if(rootNode == null){
                event.source
            }

            //if still null
            if(rootNode == null) return

            if(blockedShortRepository.isAppBlocked(event.packageName.toString())){
                when(event.packageName){
                    INSTAGRAM_PACKAGE_NAME -> {
                        for (i in INSTAGRAM_REELS_VIEWID_COMPONENT){
                            val viewId = i.formatViewIdToPackageName(INSTAGRAM_PACKAGE_NAME) //eg. "com.instagram.android:id/comment_button"
                            if(rootNode.findAccessibilityNodeInfosByViewId(viewId).isEmpty()) break

                            appBackIntentWhenBlockedWithViewId(rootNode, INSTAGRAM_VIEWID_TARGET_WHEN_REELS_BLOCK)
                        }
                    }

                    WHATSAPP_PACKAGE_NAME -> {
                        for (i in WHATSAPP_STORY_VIEWID_COMPONENT){
                            val viewId = i.formatViewIdToPackageName(WHATSAPP_PACKAGE_NAME)
                            if(rootNode.findAccessibilityNodeInfosByViewId(viewId).isEmpty()) break

                            appBackIntentWhenBlockedWithViewId(rootNode, WHATSAPP_VIEWID_TARGET_WHEN_STORY_BLOCK)
                        }
                    }
                    YOUTUBE_PACKAGE_NAME -> {
                        val viewId = YOUTUBE_SHORT_VIEWID_COMPONENT.formatViewIdToPackageName(YOUTUBE_PACKAGE_NAME)
                        if(rootNode.findAccessibilityNodeInfosByViewId(viewId).isNotEmpty()) {
                            appBackIntentWhenBlockedWithGlobalAction(GLOBAL_ACTION_BACK)
                        }
                    }
                }
            }
        }
    }

    private fun readUIElements(node: AccessibilityNodeInfo?) {
        if (node == null) return

        val text = node.text?.toString() ?: ""
        val contentDesc = node.contentDescription?.toString() ?: ""
        val className = node.className?.toString() ?: ""
        val viewId = node.viewIdResourceName ?: ""
        val isClickable = node.isClickable
        val isEnabled = node.isEnabled
        val isFocused = node.isFocused

        if (text.isNotEmpty() || contentDesc.isNotEmpty()) {
            Log.d("AccessibilityNode", """
            Text: $text
            Content Description: $contentDesc
            Class: $className
            View ID: $viewId
            Clickable: $isClickable
            Enabled: $isEnabled
            Focused: $isFocused
            Bounds: ${node.getBoundsInScreen(Rect())}
        """.trimIndent())
        }

        for (i in 0 until node.childCount) {
            val childNode = node.getChild(i)
            readUIElements(childNode)
            childNode?.recycle()
        }
    }

    override fun onInterrupt() {
    }

    private fun intentToAndroidHome(){
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        startActivity(intent)
    }

    // kalo ada bug ganti pas balik intent home pakai coroutines delay aja
    private fun appBackIntentWhenBlockedWithViewId(rootNode: AccessibilityNodeInfo, viewIdClickedTarget: String){
        val profileNode = rootNode.findAccessibilityNodeInfosByViewId(viewIdClickedTarget).first()
        profileNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        Thread.sleep(THREAD_SLEEP_BEFORE_EXIT_APP)
        intentToAndroidHome()
    }

    private fun appBackIntentWhenBlockedWithGlobalAction(globalAction: Int){
        performGlobalAction(globalAction)
        Thread.sleep(THREAD_SLEEP_BEFORE_EXIT_APP)
        intentToAndroidHome()
    }

    companion object {
        const val THREAD_SLEEP_BEFORE_EXIT_APP = 1000L
        //PACKAGE NAME
        const val INSTAGRAM_PACKAGE_NAME = "com.instagram.android"
        const val WHATSAPP_PACKAGE_NAME = "com.whatsapp"
        const val YOUTUBE_PACKAGE_NAME = "com.google.android.youtube"
        //CLICKED WHEN BLOCK VIEWID
        const val INSTAGRAM_VIEWID_TARGET_WHEN_REELS_BLOCK = "com.instagram.android:id/profile_tab"
        const val WHATSAPP_VIEWID_TARGET_WHEN_STORY_BLOCK = "com.whatsapp:id/back"
        const val YOUTUBE_ACTION_TARGET_WHEN_SHORT_BLOCK = GLOBAL_ACTION_BACK
        //COMPONENT TO BLOCK
        val INSTAGRAM_REELS_VIEWID_COMPONENT = listOf("like_button", "comment_button", "direct_share_button", "clips_ufi_more_button_component", "media_album_art_button")
        val WHATSAPP_STORY_VIEWID_COMPONENT = listOf("status_like_button", "back", "profile_picture", "name", "date", "menu")
        const val YOUTUBE_SHORT_VIEWID_COMPONENT = "reel_progress_bar"
    }
}