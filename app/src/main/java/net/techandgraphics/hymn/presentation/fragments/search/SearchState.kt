package net.techandgraphics.hymn.presentation.fragments.search

import androidx.paging.PagingData
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Search

data class SearchState(
  val lyric: PagingData<Lyric> = PagingData.empty(),
  val search: List<Search> = emptyList()
)
