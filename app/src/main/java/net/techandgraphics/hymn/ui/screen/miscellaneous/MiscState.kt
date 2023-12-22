package net.techandgraphics.hymn.ui.screen.miscellaneous

import net.techandgraphics.hymn.data.local.entities.EssentialEntity
import net.techandgraphics.hymn.data.local.entities.LyricEntity

data class MiscState(
  val favorites: List<LyricEntity> = emptyList(),
  val complementary: List<EssentialEntity> = emptyList()
)
