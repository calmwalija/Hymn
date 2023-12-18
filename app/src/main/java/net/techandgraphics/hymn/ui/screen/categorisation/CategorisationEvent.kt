package net.techandgraphics.hymn.ui.screen.categorisation

import net.techandgraphics.hymn.data.local.entities.LyricEntity

sealed class CategorisationEvent {
  class Favorite(val data: LyricEntity) : CategorisationEvent()
}
