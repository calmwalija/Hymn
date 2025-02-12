package net.techandgraphics.hymn.ui.screen.main

import net.techandgraphics.hymn.data.local.Translation
import net.techandgraphics.hymn.domain.repository.LyricRepository

data class Suggested(val english: List<Int>, val chichewa: List<Int>)

suspend fun generateSuggested(repository: LyricRepository): Suggested {
  val englishSize = repository.getLastHymn(Translation.EN.lowercase())
  val chichewaSize = repository.getLastHymn(Translation.CH.lowercase())
  val forTheWeekEnglish = (1..englishSize).toList().shuffled().take(10)
  val forTheWeekChichewa = (1..chichewaSize).toList().shuffled().take(10)
  return Suggested(forTheWeekEnglish, forTheWeekChichewa)
}
