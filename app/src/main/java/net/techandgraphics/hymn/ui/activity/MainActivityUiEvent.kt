package net.techandgraphics.hymn.ui.activity

import androidx.compose.ui.text.font.FontFamily

sealed interface MainActivityUiEvent {
  data class DynamicColor(val isEnabled: Boolean) : MainActivityUiEvent
  data class FontStyle(val fontFamily: FontFamily?) : MainActivityUiEvent
}
