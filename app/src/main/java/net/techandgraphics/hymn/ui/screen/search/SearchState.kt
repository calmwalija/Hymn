package net.techandgraphics.hymn.ui.screen.search

import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Search

data class SearchState(
  val lyrics: List<Lyric> = emptyList(),
  val search: List<Search> = emptyList(),
  var searchQuery: String = "",
  var isSearching: Boolean = false,
)
