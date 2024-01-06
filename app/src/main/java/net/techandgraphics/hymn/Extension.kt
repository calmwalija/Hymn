package net.techandgraphics.hymn

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.first
import net.techandgraphics.hymn.data.prefs.AppPrefs
import net.techandgraphics.hymn.domain.model.Lyric

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

infix fun Context.addRemoveFavoriteToast(lyric: Lyric) {
  val msg = getString(
    if (!lyric.favorite) R.string.add_favorite else R.string.remove_favorite,
    lyric.number
  )
  this toast msg
}

suspend fun AppPrefs.fontSize(): Int = getPrefs(fontKey).first()?.toString()?.toInt() ?: 1
