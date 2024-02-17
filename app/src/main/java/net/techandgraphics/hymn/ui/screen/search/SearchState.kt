package net.techandgraphics.hymn.ui.screen.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Search

data class SearchState(
  val lyricsPaged: Flow<PagingData<Lyric>> = emptyFlow(),
  val search: List<Search> = emptyList(),
  var searchQuery: String = "",
  var isSearching: Boolean = false,
)
