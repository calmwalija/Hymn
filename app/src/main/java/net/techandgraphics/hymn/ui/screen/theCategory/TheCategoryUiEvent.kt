package net.techandgraphics.hymn.ui.screen.theCategory

import net.techandgraphics.hymn.domain.model.Lyric

sealed class TheCategoryUiEvent {
  class Favorite(val data: Lyric) : TheCategoryUiEvent()
}
