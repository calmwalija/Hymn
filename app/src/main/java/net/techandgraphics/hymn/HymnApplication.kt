package net.techandgraphics.hymn

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HymnApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        onCreateNotificationChannel()
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

 }