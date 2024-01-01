package net.techandgraphics.hymn.ui.screen.miscellaneous

import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Other

data class MiscState(
  val favorites: List<Lyric> = emptyList(),
  val complementary: List<Other> = emptyList(),
  val fontSize: Int = 2,
  val lang: Lang = Lang.EN,
)
