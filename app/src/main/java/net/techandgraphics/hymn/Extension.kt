package net.techandgraphics.hymn

import android.content.Context
import android.widget.Toast
import androidx.datastore.preferences.core.longPreferencesKey
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.ui.screen.main.Suggested
import net.techandgraphics.hymn.ui.screen.main.generateSuggested
import java.io.File
import kotlin.random.Random

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
  val versionEntity = resources.getStringArray(R.array.translation_entries)
  val versionValue = resources.getStringArray(R.array.translation_values)
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

suspend fun DataStorePrefs.suggested(repository: LyricRepository): Suggested {
  val suggested = generateSuggested(repository)
  val english = mutableListOf<Int>()
  val chichewa = mutableListOf<Int>()

  val english4Week = get<String>(englishSuggestedForTheWeekKey, "")
  val chichewa4Week = get<String>(chichewaSuggestedForTheWeekKey, "")

  if (get<Long>(uniquelyCraftedMills, 0L) == null) {
    if (english4Week == null) {
      put<String>(englishSuggestedForTheWeekKey, suggested.english.joinToString(","))
      suggested(repository)
    }

    if (chichewa4Week == null) {
      put<String>(chichewaSuggestedForTheWeekKey, suggested.chichewa.joinToString(","))
      suggested(repository)
    }
  } else {
    when (val toAgo = get<Long>(uniquelyCraftedMills, 0L)!!.toAgo(System.currentTimeMillis())) {
      is Ago.Minutes -> if (toAgo.value > 1) {
        remove(longPreferencesKey(englishSuggestedForTheWeekKey))
        remove(longPreferencesKey(chichewaSuggestedForTheWeekKey))
        suggested(repository)
      }

      else -> Unit
    }
  }

  english4Week?.let { it.split(",").map { it.toInt() }.also { english.addAll(it) } }
  chichewa4Week?.let { it.split(",").map { it.toInt() }.also { chichewa.addAll(it) } }

  return Suggested(english, chichewa)
}

suspend infix fun DataStorePrefs.uniquelyCraftedKey(maxValue: Int): String {
  return if (get<Long>(uniquelyCraftedMills, 0L) == null) {
    val keys = mutableListOf<Int>()
    repeat(2) { Random.nextInt(1, maxValue).also { keys.add(it) } }
    put(uniquelyCraftedKey, keys.toString())
    put(uniquelyCraftedMills, System.currentTimeMillis())
    get(uniquelyCraftedKey)
  } else {
    when (val toAgo = get<Long>(uniquelyCraftedMills, 0L)!!.toAgo(System.currentTimeMillis())) {
      is Ago.Days -> if (toAgo.value > 1) {
        remove(longPreferencesKey(uniquelyCraftedMills))
        uniquelyCraftedKey(maxValue)
      }

      else -> get(uniquelyCraftedKey)
    }
    get(uniquelyCraftedKey)
  }
}

fun Context.fontFile() = File(cacheDir, "temp_font.ttf")
