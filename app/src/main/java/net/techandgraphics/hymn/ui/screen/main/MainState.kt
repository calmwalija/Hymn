package net.techandgraphics.hymn.ui.screen.main

import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.domain.model.Lyric

data class MainState(
  val uniquelyCrafted: List<Lyric> = emptyList(),
  val diveInto: List<Lyric> = emptyList(),
  val lang: String = Lang.EN.lowercase(),
  val onLangInvoke: Boolean = false
)
