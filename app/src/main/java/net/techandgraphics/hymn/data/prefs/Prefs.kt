package net.techandgraphics.hymn.data.prefs

import android.content.Context
import androidx.preference.PreferenceManager
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.local.Lang
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs @Inject constructor(val context: Context) {

  private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

  val lang = prefs.getString(context.getString(R.string.translation_key), Lang.EN.lowercase())!!
  val fontSize = prefs.getInt(context.getString(R.string.font_key), 2)

  fun setLang(lang: String) =
    prefs.edit().putString(context.getString(R.string.translation_key), lang).apply()

  fun setFontSize(size: Int) =
    prefs.edit().putInt(context.getString(R.string.font_key), size).apply()
}
