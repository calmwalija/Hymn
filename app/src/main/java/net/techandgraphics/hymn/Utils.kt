package net.techandgraphics.hymn

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

object Utils {

  const val FEATURE_LIMIT = 50

  fun readJsonFromAssetToString(context: Context, file: String): String? {
    return try {
      context.assets.open(file).bufferedReader().use { it.readText() }
    } catch (e: Exception) {
      null
    }
  }

  fun String.regexLowerCase() = replace(Regex("[_',.;!-\"?]"), "").lowercase()
  fun String.capitaliseWord() = split(" ").joinToString(" ") {
    it.replaceFirstChar {
      if (it.isLowerCase()) it.titlecase(
        Locale.getDefault()
      ) else it.toString()
    }
  }

  fun toast(context: Context, message: String) =
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

  fun currentMillsDiff(timeInMills: Long): Boolean {
    val today = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis())
    val diff = TimeUnit.MILLISECONDS.toDays(timeInMills)
    return today != diff
  }

  fun getThreshold(maxValue: Int, size: Int, random: Int): Int {
    val percentage = random.toFloat().div(maxValue).times(100).toInt()
    val range = size.toFloat().div(100).times(percentage).toInt()
    return if (range.plus(FEATURE_LIMIT) > size) range.minus(FEATURE_LIMIT) else range
  }
}

infix fun NavController.onChangeBook(str: String) {
  val versionEntity = context.resources.getStringArray(R.array.version_entries)
  val versionValue = context.resources.getStringArray(R.array.version_values)
  val fragmentId = currentDestination?.id
  popBackStack(fragmentId!!, true)
  navigate(fragmentId)
  val versionName = if (str == versionValue[0]) versionEntity.first() else versionEntity.last()
  val msg = "You are now reading $versionName version."
  Utils.toast(context, msg)
}

fun FirebaseAnalytics.tagEvent(name: String, bundle: Bundle) {
  logEvent(name, bundle)
}

fun timeInMillisMonth(month: Int = 1) =
  Calendar.getInstance().apply { add(Calendar.MONTH, month) }.timeInMillis
