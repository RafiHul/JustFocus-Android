package com.rafih.justfocus.presentation.ui.usagestats.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
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
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage

@Composable
fun CardItemApp(appIcon: Drawable, appName: String, appTotalUsed: String){
    Card(shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(Color("#F4F7FF".toColorInt())),
        modifier = Modifier
            .size(345.dp, 70.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, top = 10.dp, bottom = 10.dp, end = 12.dp)){

            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color.White, shape = CircleShape)
                    .size(50.dp)
            ) {
                AsyncImage(model = appIcon, contentDescription = null, modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape))
            }

            Column(Modifier.padding(start = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Outlined.DateRange,contentDescription = null, modifier = Modifier.size(15.dp,15.dp))
                    Text(appTotalUsed, fontSize = 10.sp, fontFamily = FontFamily.Serif, fontStyle = FontStyle.Normal, modifier = Modifier.padding(start = 2.dp))
                }
                Text(appName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = null
            )
        }
    }
}