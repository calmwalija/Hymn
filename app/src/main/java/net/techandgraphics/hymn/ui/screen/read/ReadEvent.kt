package net.techandgraphics.hymn.ui.screen.read

import net.techandgraphics.hymn.data.local.entities.LyricEntity

sealed class ReadEvent {
  class Click(val number: Int) : ReadEvent()
  class Favorite(val data: LyricEntity) : ReadEvent()
  class Read(val data: LyricEntity) : ReadEvent()
  class FontSize(val size: Int) : ReadEvent()
}
