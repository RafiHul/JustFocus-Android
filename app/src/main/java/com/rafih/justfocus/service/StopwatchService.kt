package com.rafih.justfocus.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.rafih.justfocus.MainActivity
import com.rafih.justfocus.R
import com.rafih.justfocus.domain.connector.StopwatchServiceConnector
import com.rafih.justfocus.domain.model.StopwatchDuration
import com.rafih.justfocus.domain.model.StopwatchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class StopwatchService: Service() {

    private val binder = StopwatchServiceBinder()
    private val handler = Handler(Looper.getMainLooper())

    private val _stopwatchState = MutableStateFlow(StopwatchState(isRunning = false))
    val stopwatchState: Flow<StopwatchState> = _stopwatchState

    private val _stopwatchDuration = MutableStateFlow<StopwatchDuration?>(null)

    private var stopwatchRunnable = object : Runnable {
        override fun run() {
            if(_stopwatchState.value.isRunning) {

                var seconds = _stopwatchState.value.seconds
                var minutes = _stopwatchState.value.minutes
                var hours = _stopwatchState.value.hours

                seconds++
                if(seconds == 60){
                    seconds = 0
                    minutes++
                }
                if(minutes == 60){
                    minutes = 0
                    hours++
                }

                checkStopWatchDuration(minutes, hours, seconds)

                _stopwatchState.value = _stopwatchState.value.copy(seconds = seconds, minutes =  minutes, hours = hours)
                handler.postDelayed(this, 1000)
                updateNotification()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = binder

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(stopwatchRunnable)
    }

    inner class StopwatchServiceBinder: Binder(), StopwatchServiceConnector {

        fun getService(): StopwatchService = this@StopwatchService

        override fun startStopwatch() {

            if (!_stopwatchState.value.isRunning) {
                _stopwatchState.value = _stopwatchState.value.copy(isRunning = true)
                handler.post(stopwatchRunnable)
            }
        }

        override fun pauseStopwatch() {
            if (_stopwatchState.value.isRunning) {
                _stopwatchState.value = _stopwatchState.value.copy(isRunning = false)
                handler.removeCallbacks(stopwatchRunnable)
            }
        }

        override fun stopStopwatch() {
            _stopwatchState.value = StopwatchState(isRunning = false)
            _stopwatchDuration.value = null
            handler.removeCallbacks(stopwatchRunnable)
            updateNotification()
        }

        override fun setStopwatchDuration(stopwatchDuration: StopwatchDuration) {
            _stopwatchDuration.value = stopwatchDuration
        }

        override fun getStopwatchState() = stopwatchState
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val formattedTime = _stopwatchState.value.let { String.format("%02d:%02d:%02d", it.hours, it.minutes, it.seconds) }

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Stopwatch Berjalan")
            .setContentText(formattedTime)
            .setSmallIcon(R.drawable.outline_timer_24)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun createNotificationChannel(){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )

        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun updateNotification() {
        getSystemService(NotificationManager::class.java)
            .notify(NOTIFICATION_ID, createNotification())
    }

    private fun checkStopWatchDuration(minutes: Int, hours: Int, second: Int){
        _stopwatchDuration.value?.let {
            if(hours >= it.hour && minutes >= it.minute && second >= it.second){
                binder.pauseStopwatch()  // TODO: ini ada bug dia harus nya stop aja, kan ini pause dia tetap jalan di sistem
            }
        }
    }

    private companion object{
        const val NOTIFICATION_CHANNEL_ID = "stopwatch_channel"
        const val NOTIFICATION_CHANNEL_NAME = "Stopwatch service"
        const val NOTIFICATION_ID = 1
    }
}