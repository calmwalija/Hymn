package net.techandgraphics.hymn.ui.screen.miscellaneous

import net.techandgraphics.hymn.domain.model.Lyric

sealed class MiscEvent {
  class RemoveFav(val data: Lyric) : MiscEvent()
  object OpenFeedback : MiscEvent()
  object OpenRating : MiscEvent()
  object OpenFavorite : MiscEvent()
  object OpenCreed : MiscEvent()
  object OpenLordsPrayer : MiscEvent()
}
