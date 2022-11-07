package net.techandgraphics.hymn.services

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random
import net.techandgraphics.hymn.HymnApplication.Companion.NOTIFICATION_ID
import net.techandgraphics.hymn.R

fun Context.getNotificationCompat(): NotificationCompat.Builder {
  return NotificationCompat.Builder(this, NOTIFICATION_ID)
}

fun getBitmapFromUrl(imageUrl: String): Bitmap? {
  return try {
    val url = URL(imageUrl)
    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
    connection.doInput = true
    connection.connect()
    val input: InputStream = connection.inputStream
    BitmapFactory.decodeStream(input)
  } catch (e: Exception) {
    null
  }
}

class NotificationService : FirebaseMessagingService() {

  override fun onMessageReceived(p0: RemoteMessage) {
    super.onMessageReceived(p0)
    showNotification(p0)
  }

  @Suppress("RedundantOverride")
  override fun onNewToken(p0: String) {
    super.onNewToken(p0)
  }

  private fun Context.showNotification(p0: RemoteMessage) {
    val data = p0.notification!!
    getNotificationCompat().let {
      it.setSmallIcon(R.drawable.ic_stat_name)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setColor(ContextCompat.getColor(this, R.color.yellow))
        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
        .setAutoCancel(true)
        .setDefaults(Notification.DEFAULT_ALL)
        .setContentText(data.body)
        .setContentTitle(data.title)
      if (data.imageUrl != null) {
        val bitmap: Bitmap? = getBitmapFromUrl(data.imageUrl.toString())
        it.setStyle(
          NotificationCompat.BigPictureStyle()
            .bigPicture(bitmap)
        )
      }
      NotificationManagerCompat.from(this).notify(Random.nextInt(), it.build())
    }
  }
}