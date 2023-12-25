package net.techandgraphics.hymn.ui.screen.categorisation

import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.domain.model.Lyric

data class CategorisationState(
  val lyric: List<Lyric> = emptyList(),
  val category: List<Category> = emptyList(),
)
