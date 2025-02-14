package net.techandgraphics.hymn.ui.screen.preview

import net.techandgraphics.hymn.domain.model.Lyric

sealed interface PreviewUiEvent {
  class Favorite(val data: Lyric) : PreviewUiEvent
  class FontSize(val size: Int) : PreviewUiEvent
  class Invoke(val theNumber: Int) : PreviewUiEvent
  data object ChangeTranslation : PreviewUiEvent
  data object GoToTheCategory : PreviewUiEvent
  data object PopBackStack : PreviewUiEvent

  sealed interface Analytics {
    data object FontDialog : PreviewUiEvent
    data object GotoTheCategory : PreviewUiEvent
    data object SwipeToLeft : PreviewUiEvent
    data object SwipeToRight : PreviewUiEvent
    data class GotoNextHymn(val theNumber: Int) : PreviewUiEvent
    data class GotoPreviousHymn(val theNumber: Int) : PreviewUiEvent
  }
}
