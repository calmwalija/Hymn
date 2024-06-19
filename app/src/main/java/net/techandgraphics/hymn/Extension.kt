package net.techandgraphics.hymn

import android.content.Context
import android.widget.Toast
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
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

infix fun Context.onTranslationChange(str: String) {
  val versionEntity = resources.getStringArray(R.array.version_entries)
  val versionValue = resources.getStringArray(R.array.version_values)
  val versionName = if (str == versionValue[0]) versionEntity.first() else versionEntity.last()
  val msg = getString(R.string.change_book_translation, versionName)
  this toast msg
}

infix fun Context.addRemoveFavoriteToast(lyric: Lyric) {
  val msg = getString(
    if (!lyric.favorite) R.string.add_favorite else R.string.remove_favorite,
    lyric.number
  )
  this toast msg
}

suspend fun DataStorePrefs.fontSize(): Int = get(fontKey, 1.toString()).toInt()
