package net.techandgraphics.hymn.ui.screen.preview

import net.techandgraphics.hymn.domain.model.Lyric

sealed class PreviewUiEvent {
  class Click(val number: Int) : PreviewUiEvent()
  class Favorite(val data: Lyric) : PreviewUiEvent()
  class FontSize(val size: Int) : PreviewUiEvent()
  class HorizontalDragGesture(val direction: Direction) : PreviewUiEvent()
  data object TranslationInverse : PreviewUiEvent()
}
