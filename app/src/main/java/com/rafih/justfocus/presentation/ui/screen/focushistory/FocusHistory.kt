package com.rafih.justfocus.presentation.ui.screen.focushistory

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rafih.justfocus.data.model.FocusHistory
import com.rafih.justfocus.domain.model.FocusHistoryData
import com.rafih.justfocus.domain.formatDate
import com.rafih.justfocus.domain.formatMillsDurationToString

@Composable
fun FocusHistory(
    modifier: Modifier,
    viewModel: FocusHistoryViewModel = hiltViewModel()
) {
    val focusHistoryData = viewModel.focusHistoryData.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFocusHistoryData()
    }

    LazyColumn() {
        items(focusHistoryData.value){
            FocusHistoryItemParent(it)
        }
    }
}

@Composable
fun FocusHistoryItemParent(data: FocusHistoryData) {

    var expanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier
        .apply{ if (expanded) wrapContentHeight() else height(100.dp) }
        .animateContentSize()) {
        Column {
            Row(
                modifier = Modifier
                    .padding(vertical = 25.dp, horizontal = 25.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(data.calendar.formatDate(), fontSize = 20.sp)
                    Text(
                        data.focusDurationTotalMills.formatMillsDurationToString(),
                        fontSize = 16.sp
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Rounded.KeyboardArrowDown, "")
                }
            }

            if (expanded) {
                LazyRow {
                    items(data.data) {
                        FocusHistoryItemChild(it)
                    }
                }
            }
        }
    }
}

@Composable
fun FocusHistoryItemChild(data: FocusHistory) {
    val totalMills = data.focusTimeMillsStop - data.focusTimeMillsStart

    Card(
        modifier = Modifier
            .size(100.dp)
        , colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Kegiatan", fontSize = 16.sp)
            Text(totalMills.formatMillsDurationToString(), fontSize = 20.sp)
        }
    }
}