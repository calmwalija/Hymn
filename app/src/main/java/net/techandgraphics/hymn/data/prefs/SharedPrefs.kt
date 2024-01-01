package net.techandgraphics.hymn.data.prefs

import android.content.Context
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.local.Lang
import javax.inject.Inject

class SharedPrefs @Inject constructor(val context: Context) {

  private val prefs = context.getSharedPreferences(
    context.getString(R.string.shared_prefs),
    Context.MODE_PRIVATE
  )

  val lang = prefs.getString(context.getString(R.string.translation_key), Lang.EN.lowercase())!!

  fun setLang(lang: String) =
    prefs.edit().putString(context.getString(R.string.translation_key), lang).apply()
}
