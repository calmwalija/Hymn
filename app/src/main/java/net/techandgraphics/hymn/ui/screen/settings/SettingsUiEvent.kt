package net.techandgraphics.hymn.ui.screen.settings

import net.techandgraphics.hymn.domain.model.Lyric

sealed class SettingsUiEvent {
  class RemoveFav(val data: Lyric) : SettingsUiEvent()
  data object OpenFeedback : SettingsUiEvent()
  data object OpenRating : SettingsUiEvent()
  data object OpenFavorite : SettingsUiEvent()
  data object OpenCreed : SettingsUiEvent()
  data object OpenLordsPrayer : SettingsUiEvent()
}
