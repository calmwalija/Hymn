package net.techandgraphics.hymn.ui.screen.theCategory

import net.techandgraphics.hymn.data.local.Translation
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.domain.model.Lyric

data class TheCategoryState(
  val lyric: List<Lyric> = emptyList(),
  val category: List<Category> = emptyList(),
  val lang: Translation = Translation.EN,
)
