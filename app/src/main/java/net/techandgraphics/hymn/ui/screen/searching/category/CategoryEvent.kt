package net.techandgraphics.hymn.ui.screen.searching.category

sealed class CategoryEvent {
  class Click(val id: Int) : CategoryEvent()
  class OnSearchQuery(val searchQuery: String) : CategoryEvent()
  data object ClearSearchQuery : CategoryEvent()
}
