package net.techandgraphics.hymn.ui.screen.main

import net.techandgraphics.hymn.data.local.entities.LyricEntity

sealed class MainEvent {
  class Favorite(val data: LyricEntity) : MainEvent()
  class LanguageChange(val lang: String, val onFinish: () -> Unit) : MainEvent()
}
