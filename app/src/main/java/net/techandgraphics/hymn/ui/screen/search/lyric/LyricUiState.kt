package net.techandgraphics.hymn.ui.screen.search.lyric

import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Search

data class LyricUiState(
  val lyrics: List<Lyric> = emptyList(),
  val search: List<Search> = emptyList(),
  var searchQuery: String = "",
  var isSearching: Boolean = false,
)
