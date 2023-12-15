package net.techandgraphics.hymn.ui.screen.read

sealed class ReadEvent {
  class Click(val number: Int) : ReadEvent()
}
