package net.techandgraphics.hymn.ui.screen.main

import net.techandgraphics.hymn.data.local.entities.LyricEntity

sealed class MainEvent {
  class Timestamp(val data: LyricEntity) : MainEvent()
}
