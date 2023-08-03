package net.techandgraphics.hymn

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import net.techandgraphics.hymn.worker.HymnWorker
import net.techandgraphics.hymn.worker.HymnWorkerFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class HymnApplication : Application(), Configuration.Provider {

  @Inject
  lateinit var workerFactory: HymnWorkerFactory

  override fun onCreate() {
    super.onCreate()
    onCreateNotificationChannel()
    onCreateWorker()
  }

  companion object {
    const val NOTIFICATION_ID = "198193"
    const val NOTIFICATION_DESCRIPTION = "Hymn Channel Description"
  }

  private fun onCreateNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        .createNotificationChannel(
          NotificationChannel(
            NOTIFICATION_ID,
            NOTIFICATION_DESCRIPTION,
            NotificationManager.IMPORTANCE_HIGH
          )
        )
    }
  }

  private fun onCreateWorker() {
    val workRequest = PeriodicWorkRequestBuilder<HymnWorker>(1, TimeUnit.DAYS)
      .setInitialDelay(2, TimeUnit.MINUTES)
      .setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
      .setBackoffCriteria(
        BackoffPolicy.EXPONENTIAL,
        PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
        TimeUnit.MILLISECONDS
      ).build()
    WorkManager.getInstance(this).enqueue(workRequest)
  }

  override fun getWorkManagerConfiguration(): Configuration {
    return Configuration.Builder()
      .setWorkerFactory(workerFactory)
      .setMinimumLoggingLevel(Log.ERROR)
      .build()
  }
}
