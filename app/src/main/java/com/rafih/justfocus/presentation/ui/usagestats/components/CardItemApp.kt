package com.rafih.justfocus.presentation.ui.usagestats.components

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun CardItemApp(
    app: ApplicationInfo,
    pm: PackageManager,
    totalUsed: String,
    onNavigateToAppUsageStats: (String) -> Unit
    ){

    val appIcon = remember(app.packageName) {
        pm.getApplicationIcon(app)
    }

    val appLabel = remember(app.packageName) {
        pm.getApplicationLabel(app).toString()
    }

    Box(modifier = Modifier
        .background(Color.White)
        .fillMaxWidth()
        .wrapContentHeight()) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)){


            AsyncImage(model = appIcon, contentDescription = null, modifier = Modifier
                .size(30.dp)
                .clip(CircleShape))


            Column(Modifier.padding(start = 16.dp)) {
                Text(appLabel, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(totalUsed, fontSize = 14.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Normal)
            }

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                modifier = Modifier.clickable{
                    onNavigateToAppUsageStats(app.packageName)
                }
            )
        }
    }
}