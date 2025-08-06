package com.rafih.justfocus.presentation.ui.screen.focusmode.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rafih.justfocus.domain.timeToMilis
import com.rafih.justfocus.presentation.ui.component.timepicker.MyWheelTimePicker
import java.time.LocalTime

@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (Long) -> Unit
) {

    var stopwatchDuration by remember { mutableStateOf(0L) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                Row {
                    Text("Hour")
                    Text("Minute")
                }

                MyWheelTimePicker(startTime = LocalTime.MIDNIGHT){ snappedTime ->
                    stopwatchDuration = snappedTime.timeToMilis()
                }

                Button(onClick = {
                    if(stopwatchDuration != 0L){
                        onConfirmRequest(stopwatchDuration)
                    }
                }) {
                    Text("Confirm")
                }
            }
        }
    }
}