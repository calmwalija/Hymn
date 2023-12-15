package net.techandgraphics.hymn.ui.screen.categorisation

import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.local.join.Category

data class CategorisationState(
  val lyric: List<LyricEntity> = emptyList(),
  val category: List<Category> = emptyList(),
)
