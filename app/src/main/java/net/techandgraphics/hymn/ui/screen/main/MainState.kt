package net.techandgraphics.hymn.ui.screen.main

import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.domain.model.Lyric

data class MainState(
  val spotlight: List<Category> = emptyList(),
  val uniquelyCrafted: List<Lyric> = emptyList(),
  val diveInto: List<Lyric> = emptyList(),
  var queryId: Int = 1,
  val lang: String = Lang.EN.lowercase(),
  val onLangInvoke: Boolean = false
)
