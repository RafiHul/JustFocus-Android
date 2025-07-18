package com.rafih.justfocus

import android.Manifest
import android.app.AppOpsManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.rafih.justfocus.presentation.AppNavGraph
import com.rafih.justfocus.presentation.ui.theme.JustFocusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("cek", "asuuu")
        if (!hasUsageStatsPermission()){
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }

        if(!isAccessibilityServiceEnabled(MyAccessbilityService::class.java)){
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001
                )
            }
        }

        setContent {
            JustFocusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    AppNavGraph(navController, Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun hasUsageStatsPermission(): Boolean {
        val opsManager = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            opsManager.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
        } else {
            opsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
        }

        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun isAccessibilityServiceEnabled(serviceClass: Class<*>): Boolean {
        val expectedComponentName = ComponentName(this, serviceClass)
        val enabledServicesSetting = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        if (enabledServicesSetting == null) {
            return false
        }

        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServicesSetting)

        while (colonSplitter.hasNext()) {
            val componentNameString = colonSplitter.next()
            val enabledComponentName = ComponentName.unflattenFromString(componentNameString)

            if (enabledComponentName != null && enabledComponentName == expectedComponentName) {
                return true
            }
        }
        return false
    }
}