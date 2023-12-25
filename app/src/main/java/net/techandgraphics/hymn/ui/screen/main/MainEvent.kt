package net.techandgraphics.hymn.ui.screen.main

import net.techandgraphics.hymn.domain.model.Lyric

sealed class MainEvent {
  class Favorite(val data: Lyric) : MainEvent()
  class LanguageChange(val lang: String, val onFinish: () -> Unit) : MainEvent()
}
