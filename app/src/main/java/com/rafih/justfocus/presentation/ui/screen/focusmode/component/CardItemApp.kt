package com.rafih.justfocus.presentation.ui.screen.focusmode.component

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun CardItemApp(app: ApplicationInfo, pm: PackageManager, selected: Boolean, onCheck: () -> Unit) {

    val appsLabel = remember(app.packageName) {
        pm.getApplicationLabel(app).toString()
    }

    val appsIcon = remember(app.packageName) {
        pm.getApplicationIcon(app)
    }

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {


            AsyncImage(
                model = appsIcon, contentDescription = null, modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
            )

            Column(modifier = Modifier.weight(1f).padding(5.dp)) {
                Text(
                    text = appsLabel,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.width(8.dp))

            Checkbox(checked = selected, onCheckedChange = {
                onCheck()
            })
        }
    }
}