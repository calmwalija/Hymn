package net.techandgraphics.hymn

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavHostController
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.Calendar

infix fun Context.readJsonFromAssetToString(file: String): String? {
  return try {
    assets.open(file).bufferedReader().use { it.readText() }
  } catch (e: Exception) {
    null
  }
}

infix fun Context.toast(message: String) =
  Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

infix fun NavHostController.onLanguageChange(str: String) {
  val versionEntity = context.resources.getStringArray(R.array.version_entries)
  val versionValue = context.resources.getStringArray(R.array.version_values)
  val fragmentId = currentDestination?.id
  popBackStack(fragmentId!!, true)
  navigate(fragmentId)
  val versionName = if (str == versionValue[0]) versionEntity.first() else versionEntity.last()
  val msg = context.getString(R.string.change_book_translation, versionName)
  context toast msg
}

fun FirebaseAnalytics.tagEvent(name: String, bundle: Bundle) {
  logEvent(name, bundle)
}

fun timeInMillisMonth(month: Int = 1) =
  Calendar.getInstance().apply { add(Calendar.MONTH, month) }.timeInMillis
