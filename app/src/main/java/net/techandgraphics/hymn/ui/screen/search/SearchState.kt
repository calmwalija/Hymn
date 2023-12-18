package net.techandgraphics.hymn.ui.screen.search

import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.local.entities.SearchEntity

data class SearchState(
  val lyric: List<LyricEntity> = emptyList(),
  val search: List<SearchEntity> = emptyList(),
  var searchQuery: String = "",
  var isSearching: Boolean = false,
  var sortBy: String = searchOrders.first(),
  var filterBy: String = searchFilters.first(),
)
