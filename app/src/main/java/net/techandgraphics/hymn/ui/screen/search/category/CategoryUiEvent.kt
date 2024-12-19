package net.techandgraphics.hymn.ui.screen.search.category

sealed class CategoryUiEvent {
  class Click(val id: Int) : CategoryUiEvent()
  class OnSearchQuery(val searchQuery: String) : CategoryUiEvent()
  data object ClearSearchQuery : CategoryUiEvent()
}
