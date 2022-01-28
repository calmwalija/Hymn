package net.techandgraphics.hymn

import android.app.Application
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HymnApplication : Application() {

    companion object {
        const val NOTIFICATION_ID = "198193"
        const val NOTIFICATION_DESCRIPTION = "Hymn Channel Description"
    }

    override fun onCreate() {
        super.onCreate()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannelCompat.Builder(
                NOTIFICATION_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setDescription(NOTIFICATION_DESCRIPTION)
                setVibrationEnabled(true)
                NotificationManagerCompat.from(this@HymnApplication)
                    .createNotificationChannel(this.build())
            }
        }
    }
}