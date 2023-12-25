package net.techandgraphics.hymn.ui.screen.main

import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.domain.model.Lyric

data class MainState(
  val spotlighted: List<Category> = emptyList(),
  val uniquelyCrafted: List<Lyric> = emptyList(),
  val theHymn: List<Lyric> = emptyList(),
  var queryId: Int = 1,
  val lang: String = Lang.EN.lowercase(),
)
