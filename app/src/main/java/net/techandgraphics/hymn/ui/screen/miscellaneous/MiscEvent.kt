package net.techandgraphics.hymn.ui.screen.miscellaneous

import net.techandgraphics.hymn.data.local.entities.LyricEntity

sealed class MiscEvent {
  class RemoveFav(val data: LyricEntity) : MiscEvent()
}
