package net.techandgraphics.hymn.ui.screen.miscellaneous

import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.local.entities.OtherEntity

data class MiscState(
  val favorites: List<LyricEntity> = emptyList(),
  val complementary: List<OtherEntity> = emptyList()
)
