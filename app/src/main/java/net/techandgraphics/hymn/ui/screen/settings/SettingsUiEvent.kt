package net.techandgraphics.hymn.ui.screen.settings

import android.net.Uri
import androidx.compose.ui.text.font.FontFamily
import net.techandgraphics.hymn.domain.model.Lyric

sealed class SettingsUiEvent {
  class RemoveFav(val data: Lyric) : SettingsUiEvent()
  class Import(val uri: Uri) : SettingsUiEvent()
  data class DynamicColor(val isEnabled: Boolean) : SettingsUiEvent()
  data object Export : SettingsUiEvent()
  data object OpenFeedback : SettingsUiEvent()
  data object OpenRating : SettingsUiEvent()
  data object OpenFavorite : SettingsUiEvent()
  data object OpenCreed : SettingsUiEvent()
  data object OpenLordsPrayer : SettingsUiEvent()

  sealed class Font {
    data class Selected(val fontFamily: FontFamily?, val fontName: String?) : SettingsUiEvent()
    data class Apply(val fontFamily: FontFamily?) : SettingsUiEvent()
    data object Default : SettingsUiEvent()
    data object Choose : SettingsUiEvent()
  }
}
