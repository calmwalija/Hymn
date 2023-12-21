package net.techandgraphics.hymn

import android.content.Context
import java.util.Locale

fun String.capitalizeFirst() =
  lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun String.hymnCount(context: Context): String {
  if (contains("-").not())
    return context.resources.getQuantityString(R.plurals.hymn_count, toInt(), toInt())
  else
    take(indexOf("-")).toInt().also {
      return context.resources.getQuantityString(R.plurals.hymn_count, it, it)
    }
}

fun String.removeSymbols() =
  replace(Regex("[_',.;!-\"?]"), "").lowercase()

fun String.capitaliseWord() = split(" ").joinToString(" ") {
  it.replaceFirstChar { char ->
    if (char.isLowerCase()) char.titlecase(
      Locale.getDefault()
    ) else it
  }
}
