package net.techandgraphics.hymn

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val SECOND_MILLIS = 1000
const val MINUTE_MILLIS = 60 * SECOND_MILLIS
const val HOUR_MILLIS = 60 * MINUTE_MILLIS

fun Long.toTimeAgo(context: Context): String {
  val diff = System.currentTimeMillis() - this
  return when {
    diff < MINUTE_MILLIS -> "just now"
    diff < 60 * MINUTE_MILLIS -> {
      val minutes = diff.div(MINUTE_MILLIS).toInt()
      "${context.resources.getQuantityString(R.plurals.minutes, minutes, minutes)} ago"
    }

    diff < 24 * HOUR_MILLIS -> {
      val hours = diff.div(HOUR_MILLIS).toInt()
      "${context.resources.getQuantityString(R.plurals.hours, hours, hours)} ago"
    }

    else -> longDateFormat(this)
  }
}

private fun longDateFormat(timestamp: Long): String {
  val simpleDateFormat = SimpleDateFormat("dd MMM, yyyy ", Locale.getDefault())
  val currentTimeMillis = Date(timestamp)
  return simpleDateFormat.format(currentTimeMillis)
}
