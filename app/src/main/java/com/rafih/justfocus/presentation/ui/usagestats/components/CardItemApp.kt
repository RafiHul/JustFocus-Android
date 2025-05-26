package com.rafih.justfocus.presentation.ui.usagestats.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun CardItemApp(appIcon: Drawable, appName: String, appTotalUsed: String){
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
                Text(appName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(appTotalUsed, fontSize = 14.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Normal)
            }

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null
            )
        }
    }
}