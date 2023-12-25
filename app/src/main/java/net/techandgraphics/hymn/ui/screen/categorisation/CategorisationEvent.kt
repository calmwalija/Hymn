package net.techandgraphics.hymn.ui.screen.categorisation

import net.techandgraphics.hymn.domain.model.Lyric

sealed class CategorisationEvent {
  class Favorite(val data: Lyric) : CategorisationEvent()
}
