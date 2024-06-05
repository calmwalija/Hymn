package net.techandgraphics.hymn.ui.screen.read

import net.techandgraphics.hymn.domain.model.Lyric

sealed class ReadEvent {
  class Click(val number: Int) : ReadEvent()
  class Favorite(val data: Lyric) : ReadEvent()
  class FontSize(val size: Int) : ReadEvent()
  class HorizontalDragGesture(val direction: Direction) : ReadEvent()
  data object TranslationInverse : ReadEvent()
}
