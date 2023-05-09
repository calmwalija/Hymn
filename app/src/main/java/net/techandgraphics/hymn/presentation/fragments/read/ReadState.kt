package net.techandgraphics.hymn.presentation.fragments.read

import net.techandgraphics.hymn.domain.model.Lyric

data class ReadState(
  val lyric: List<Lyric> = emptyList()
)
