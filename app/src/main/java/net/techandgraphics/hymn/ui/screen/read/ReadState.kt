package net.techandgraphics.hymn.ui.screen.read

data class ReadState(
  val lyricEntityKey: List<LyricEntityKey> = emptyList(),
  val fontSize: Int = 1
)
