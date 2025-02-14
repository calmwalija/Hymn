package net.techandgraphics.hymn.ui.screen.main

import net.techandgraphics.hymn.data.local.Translation
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Other
import net.techandgraphics.hymn.domain.model.Search

data class MainUiState(
  val uniquelyCrafted: List<Lyric> = emptyList(),
  val diveInto: List<Lyric> = emptyList(),
  val translation: String = Translation.EN.lowercase(),
  val categories: List<Category> = emptyList(),
  val lyrics: List<Lyric> = emptyList(),
  val favorites: List<Lyric> = emptyList(),
  val emptyStateSuggestedLyrics: List<Lyric> = emptyList(),
  var isSearching: Boolean = false,
  val search: List<Search> = emptyList(),
  var searchQuery: String = "",
  var fontSize: Int = 1,
  val theCreedAndLordsPrayer: List<Other> = emptyList(),
)
