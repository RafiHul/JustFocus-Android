package com.rafih.justfocus

import android.accessibilityservice.AccessibilityService
import android.app.AppOpsManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.rafih.justfocus.presentation.AppNavGraph
import com.rafih.justfocus.presentation.ui.theme.JustFocusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (!hasUsageStatsPermission()){
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }

//        if(!hasAccessbilityPermission(MyAccessbilityService())){
//            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//            startActivity(intent)
//        }

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

    private fun hasAccessbilityPermission(service: AccessibilityService): Boolean {
        val enabledService = Settings.System.getString(
            contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledService)

        val myService = ComponentName(this, service::class.java)
        for(serviceName in colonSplitter){
            if(serviceName.equals(myService.toString(), ignoreCase = true)){
                return true
            }
        }

        return false
    }
}