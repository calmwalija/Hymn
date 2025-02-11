package net.techandgraphics.hymn.ui.screen.preview

data class PreviewUiState(
  val previewLyricKey: List<PreviewLyricKey> = emptyList(),
  val previewLyricKeyInverse: List<PreviewLyricKey> = emptyList(),
  val translationInverse: Boolean = false,
  val lyrics: List<PreviewLyricKey> = emptyList(),
  val fontSize: Int = 1,
  val gotToPrevHymn: Int = -1,
  val gotToNextHymn: Int = -1,
)
