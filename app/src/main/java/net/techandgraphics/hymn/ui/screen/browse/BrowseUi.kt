package net.techandgraphics.hymn.ui.screen.browse

enum class BrowseSort {
  Number,
  Title,
  Categories,
}

data class BrowseUiState(
  val query: String = "",
  val sort: BrowseSort = BrowseSort.Number,
  val favoritesOnly: Boolean = false,
  val hymns: List<net.techandgraphics.hymn.domain.model.Lyric> = emptyList(),
  val categories: List<net.techandgraphics.hymn.domain.model.Category> = emptyList(),
)

sealed interface BrowseUiEvent {
  data class Query(val value: String) : BrowseUiEvent
  data class Sort(val sort: BrowseSort) : BrowseUiEvent
  data object ToggleFavoritesOnly : BrowseUiEvent
  data class OpenHymn(val number: Int) : BrowseUiEvent
  data class OpenCategory(val categoryId: Int) : BrowseUiEvent
  data class Favorite(val number: Int, val favorite: Boolean) : BrowseUiEvent
}
