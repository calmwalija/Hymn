package net.techandgraphics.hymn.ui.activity

sealed interface MainActivityUiEvent {
  data class DynamicColor(val isEnabled: Boolean) : MainActivityUiEvent
}
