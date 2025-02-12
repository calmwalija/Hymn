package net.techandgraphics.hymn.ui.screen.preview

import net.techandgraphics.hymn.data.local.Translation
import net.techandgraphics.hymn.domain.model.Lyric

data class PreviewUiState(
  val lyrics: List<Lyric> = emptyList(),
  val fontSize: Int = 1,
  val gotToPrevHymn: Int = -1,
  val gotToNextHymn: Int = -1,
  val currentLyric: Lyric? = null,
  val categoryId: Int = 0,
  val theHymnNumber: Int = 0,
  val lyricsWithIndex: List<LyricWithKey> = emptyList(),
  val translations: List<String> = listOf(),
  val defaultTranslation: String = Translation.EN.lowercase(),
  val currentTranslation: String = Translation.EN.lowercase()
)
