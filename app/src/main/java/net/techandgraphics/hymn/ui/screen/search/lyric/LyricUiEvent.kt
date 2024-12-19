package net.techandgraphics.hymn.ui.screen.search.lyric

import net.techandgraphics.hymn.domain.model.Search

sealed class LyricUiEvent {
  class OnLyricUiQuery(val searchQuery: String) : LyricUiEvent()
  class LyricUiQueryTag(val searchQuery: String) : LyricUiEvent()
  data object InsertLyricUiTag : LyricUiEvent()
  data object ClearLyricUiQuery : LyricUiEvent()
  class OnLongPress(val search: Search) : LyricUiEvent()
}
