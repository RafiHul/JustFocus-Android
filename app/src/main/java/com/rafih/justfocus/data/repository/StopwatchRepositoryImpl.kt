package com.rafih.justfocus.data.repository

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.rafih.justfocus.service.StopwatchService
import com.rafih.justfocus.domain.connector.StopwatchServiceConnector
import com.rafih.justfocus.domain.model.StopWatchDuration
import com.rafih.justfocus.domain.model.StopwatchState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): StopwatchServiceConnector {

    private var serviceConnector : StopwatchServiceConnector? = null
    private var isServiceBound = false

    private val _serviceConnected = MutableSharedFlow<Boolean>(replay = 1)
    val serviceConnected: Flow<Boolean> = _serviceConnected.asSharedFlow()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            serviceConnector = (service as StopwatchService.StopwatchServiceBinder)
            isServiceBound = true
            _serviceConnected.tryEmit(true)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceConnector = null
            isServiceBound = false
            _serviceConnected.tryEmit(false)
        }
    }

    fun bindService() {
        if (!isServiceBound) {
            val intent = Intent(context, StopwatchService::class.java)
            context.startForegroundService(intent)
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun unbindService(){ //use for stop stopwatch
        if (isServiceBound && serviceConnector != null) {
            context.unbindService(serviceConnection)
            context.stopService(Intent(context, StopwatchService::class.java))
            serviceConnector = null
            isServiceBound = false
            _serviceConnected.tryEmit(false)
        }
    }

    override fun startStopwatch() {
        serviceConnector?.startStopwatch()
    }

    override fun stopStopwatch() {
//        serviceConnector?.stopStopwatch()
        serviceConnector?.stopStopwatch()
    }


    override fun pauseStopwatch() {
        serviceConnector?.pauseStopwatch()
    }

    override fun resumeStopwatch() {
        serviceConnector?.resumeStopwatch()
    }

    override fun setStopwatchDuration(stopWatchDuration: StopWatchDuration) {
        serviceConnector?.setStopwatchDuration(stopWatchDuration)
    }

    override fun getStopwatchState(): Flow<StopwatchState> {
        return serviceConnector?.getStopwatchState() ?: MutableStateFlow(StopwatchState(false))
    }
}