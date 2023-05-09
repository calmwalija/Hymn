package net.techandgraphics.hymn.presentation.fragments.favorite

import net.techandgraphics.hymn.domain.model.Lyric

data class FavoriteState(
  val mostVisited: List<Lyric> = emptyList(),
  val favorite: List<Lyric> = emptyList()
)
