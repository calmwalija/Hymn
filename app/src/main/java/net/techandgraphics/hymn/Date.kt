package net.techandgraphics.hymn

import android.content.Context
import android.content.res.Resources
import androidx.annotation.PluralsRes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val SECOND_MILLIS = 1000
const val MINUTE_MILLIS = 60 * SECOND_MILLIS
const val HOUR_MILLIS = 60 * MINUTE_MILLIS

sealed class Ago {
  data object Now : Ago()
  data class Minutes(val value: Int) : Ago()
  data class Hours(val value: Int) : Ago()
  data class Days(val value: Long) : Ago()
}

fun Long.toAgo(currentTimeMillis: Long): Ago {
  val diff = currentTimeMillis - this
  return when {
    diff < MINUTE_MILLIS -> Ago.Now
    diff < 60 * MINUTE_MILLIS -> Ago.Minutes(diff.div(MINUTE_MILLIS).toInt())
    diff < 24 * HOUR_MILLIS -> Ago.Hours(diff.div(HOUR_MILLIS).toInt())
    else -> Ago.Days(this)
  }
}

fun Long.toTimeAgo(context: Context): String {
  with(context.resources) {
    return when (val ago = toAgo(System.currentTimeMillis())) {
      Ago.Now -> "just now"
      is Ago.Minutes -> ago(R.plurals.minutes, ago.value)
      is Ago.Hours -> ago(R.plurals.hours, ago.value)
      is Ago.Days -> longDateFormat(ago.value)
    }
  }
}

private fun longDateFormat(timestamp: Long): String {
  val simpleDateFormat = SimpleDateFormat("dd MMM, yyyy ", Locale.getDefault())
  val currentTimeMillis = Date(timestamp)
  return simpleDateFormat.format(currentTimeMillis)
}

private fun Resources.ago(@PluralsRes pluralRes: Int, value: Int): String {
  return "${getQuantityString(pluralRes, value, value)} ago"
}
