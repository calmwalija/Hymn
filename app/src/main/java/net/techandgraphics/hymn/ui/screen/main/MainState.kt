package net.techandgraphics.hymn.ui.screen.main

import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.local.join.Category

data class MainState(
  val featured: List<Category> = emptyList(),
  val ofTheDay: List<LyricEntity> = emptyList(),
  val theHymn: List<LyricEntity> = emptyList(),
  var queryId: Int = 1,
)
