package net.techandgraphics.hymn.ui.screen.main

sealed interface MainChannelEvent {
  data object Language : MainChannelEvent
}
