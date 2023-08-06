package net.techandgraphics.hymn.worker

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Resource
import net.techandgraphics.hymn.Tag.NOTIFICATION_HYMN_THRESHOLD
import net.techandgraphics.hymn.data.repository.Repository
import net.techandgraphics.hymn.services.getNotificationCompat
import kotlin.random.Random

@HiltWorker
class HymnWorker @AssistedInject constructor(
  @Assisted
  val context: Context,
  @Assisted
  params: WorkerParameters,
  val repository: Repository,
) : CoroutineWorker(context, params) {

  override suspend fun doWork(): Result {
    return repository.lyricRepository.fetch {
      when (it) {
        is Resource.Success -> it.data?.let {
          val list = StringBuilder()
          var size: Int
          it.distinctBy { it.number }.also { size = it.size }.forEach {
            list.append("#").append(it.number).append(".").append(it.title).append("\n")
          }
          String.format(
            "Hooray \uD83C\uDF89 We have added %d new %s",
            size, context.resources.getQuantityString(R.plurals.hymn, size, size)
          ).also {
            context.showNotification(it, list.toString(), size)
          }
        }

        else -> Unit
      }
    }
  }

  private fun Context.showNotification(p0: String, p1: String, p2: Int) {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    intent?.putExtra(NOTIFICATION_HYMN_THRESHOLD, p2)
    val pendingIntent = PendingIntent.getActivity(
      this, 0, intent, PendingIntent.FLAG_IMMUTABLE
    )
    getNotificationCompat().let {
      it.setSmallIcon(R.drawable.ic_stat_name)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setColor(ContextCompat.getColor(this, R.color.mellon))
        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .setDefaults(Notification.DEFAULT_ALL)
        .setStyle(NotificationCompat.BigTextStyle().bigText(p1))
        .setContentTitle(p0)
      if (ActivityCompat.checkSelfPermission(
          this,
          Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        return
      }
      NotificationManagerCompat.from(this).notify(Random.nextInt(), it.build())
    }
  }
}
