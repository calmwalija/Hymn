package net.techandgraphics.hymn.ui.screen.read

import net.techandgraphics.hymn.data.local.entities.LyricEntity

data class ReadState(
  val lyrics: List<LyricEntity> = emptyList(),
)
