package net.techandgraphics.hymn.ui.screen.settings

import net.techandgraphics.hymn.domain.model.Lyric
import java.io.File

sealed class SettingsUiEvent {
  class RemoveFav(val data: Lyric) : SettingsUiEvent()
  class Import(val file: File) : SettingsUiEvent()
  data object OpenFeedback : SettingsUiEvent()
  data object OpenRating : SettingsUiEvent()
  data object OpenFavorite : SettingsUiEvent()
  data object OpenCreed : SettingsUiEvent()
  data object OpenLordsPrayer : SettingsUiEvent()
}
