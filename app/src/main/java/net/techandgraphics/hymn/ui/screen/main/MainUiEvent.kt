package net.techandgraphics.hymn.ui.screen.main

import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Search

sealed interface MainUiEvent {
  class Favorite(val data: Lyric) : MainUiEvent
  class Language(val lang: String) : MainUiEvent

  enum class OfType { Category, Preview }
  data class Event(val ofType: OfType, val id: Int) : MainUiEvent

  sealed class MenuItem {
    data object Settings : MainUiEvent
    data object ApostlesCreed : MainUiEvent
    data object LordsPrayer : MainUiEvent
    data object Favorites : MainUiEvent
  }

  sealed class LyricUiEvent {
    class OnLyricUiQuery(val searchQuery: String) : MainUiEvent
    class LyricUiQueryTag(val searchQuery: String) : MainUiEvent
    data object InsertLyricUiTag : MainUiEvent
    data object ClearLyricUiQuery : MainUiEvent
    class OnLongPress(val search: Search) : MainUiEvent
  }

  sealed class CategoryUiEvent {
    data class GoTo(val category: Category) : MainUiEvent
    data object OnViewCategories : MainUiEvent
  }
}
