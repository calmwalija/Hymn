package net.techandgraphics.hymn.ui.activity

import androidx.compose.ui.text.font.FontFamily
import net.techandgraphics.hymn.ui.theme.AppTheme

sealed interface MainActivityUiEvent {
  data class DynamicColor(val isEnabled: Boolean) : MainActivityUiEvent
  data class FontStyle(val fontFamily: FontFamily?) : MainActivityUiEvent
  data class ThemeMode(val theme: AppTheme) : MainActivityUiEvent
}
