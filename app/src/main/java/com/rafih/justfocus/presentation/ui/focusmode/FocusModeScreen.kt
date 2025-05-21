package com.rafih.justfocus.presentation.ui.focusmode

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlin.collections.set

@Composable
fun FocusModeScreen(modifier: Modifier) {
    val context = LocalContext.current
    val pm = context.packageManager

    val selectedState = remember {
        mutableStateMapOf<String, Boolean>()
    }

    val packages = pm.getInstalledApplications(0)
        .filter { (it.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0 }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        contentPadding = PaddingValues(5.dp)
    ) {
        itemsIndexed(
            packages,
            key = { _, item -> item.packageName } //ini key seperi async differ di dalam recycler view
        ) { _, item ->
            val packageName = item.packageName
            val isSelected = selectedState[packageName] == true

            AppCard(
                applicationInfo = item,
                packageManager = pm,
                isSelected = isSelected,
                onToggleSelection = {
                    selectedState[packageName] = !isSelected
                }
            )
        }
    }
}

@Composable
private fun AppCard(
    applicationInfo: ApplicationInfo,
    packageManager: PackageManager,
    isSelected: Boolean,
    onToggleSelection: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .size(100.dp)
            .background(if (isSelected) Color.Red else Color.White)
            .clickable(onClick = onToggleSelection)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = packageManager.getApplicationIcon(applicationInfo),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Text(packageManager.getApplicationLabel(applicationInfo).toString())
        }
    }
}