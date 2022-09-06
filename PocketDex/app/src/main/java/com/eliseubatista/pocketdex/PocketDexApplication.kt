package com.eliseubatista.pocketdex

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.work.*
import com.eliseubatista.pocketdex.workers.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PocketDexApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRefreshDataWork()
        }
    }

    private fun setupRefreshDataWork() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED) //Only on wifi
            .setRequiresBatteryNotLow(true) // Only with a decent battery level
            .setRequiresCharging(false) //Can be done on battery
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //If possible, only when the device is idle. Or maybe not, we'll see
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val refreshDataRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints).build()


        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "REFRESH_DATA",
            ExistingPeriodicWorkPolicy.REPLACE,
            refreshDataRequest
        )

        Log.w("APP", "Background Worker Set!")
    }
}