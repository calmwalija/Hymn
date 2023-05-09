package net.techandgraphics.hymn.presentation.fragments.main

import androidx.paging.PagingData
import net.techandgraphics.hymn.domain.model.Essential
import net.techandgraphics.hymn.domain.model.Lyric

data class MainState(
  val lyric: PagingData<Lyric> = PagingData.empty(),
  val essential: List<Essential> = emptyList()
)
