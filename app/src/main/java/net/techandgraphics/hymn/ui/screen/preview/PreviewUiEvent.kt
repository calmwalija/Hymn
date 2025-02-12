package net.techandgraphics.hymn.ui.screen.preview

import net.techandgraphics.hymn.domain.model.Lyric

sealed interface PreviewUiEvent {
  class Click(val number: Int) : PreviewUiEvent
  class Favorite(val data: Lyric) : PreviewUiEvent
  class FontSize(val size: Int) : PreviewUiEvent
  class Invoke(val theNumber: Int) : PreviewUiEvent
  data object ChangeTranslation : PreviewUiEvent
  data object GoToTheCategory : PreviewUiEvent
  data object PopBackStack : PreviewUiEvent
}
