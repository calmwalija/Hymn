package net.techandgraphics.hymn.ui.screen.read

data class ReadState(
  val lyricKey: List<LyricKey> = emptyList(),
  val lyricKeyInverse: List<LyricKey> = emptyList(),
  val translationInverse: Boolean = false,
  val lyrics: List<LyricKey> = emptyList(),
  val fontSize: Int = 1,
)
