package net.techandgraphics.hymn.ui.screen.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.domain.model.Lyric

data class SearchState(
  val lyricsPaged: Flow<PagingData<Lyric>> = emptyFlow(),
  val search: List<SearchEntity> = emptyList(),
  var searchQuery: String = "",
  var isSearching: Boolean = false,
  var sortBy: String = searchOrders.first(),
  var filterBy: String = searchFilters.first(),
)
