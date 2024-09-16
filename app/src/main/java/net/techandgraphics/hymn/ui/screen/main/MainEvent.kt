package net.techandgraphics.hymn.ui.screen.main

import net.techandgraphics.hymn.domain.model.Lyric

sealed class MainEvent {
  class Favorite(val data: Lyric) : MainEvent()
  class Language(val lang: String) : MainEvent()
  data class Goto(val navigate: Navigate) : MainEvent()
  enum class Navigate { Search, Category }
  enum class OfType { Category, Read }
  data class Event(val ofType: OfType, val id: Int) : MainEvent()
}
