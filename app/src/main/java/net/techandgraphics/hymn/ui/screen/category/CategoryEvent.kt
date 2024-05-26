package net.techandgraphics.hymn.ui.screen.category

sealed class CategoryEvent {
  class Click(val id: Int) : CategoryEvent()
  class OnSearchQuery(val searchQuery: String) : CategoryEvent()
  data object ClearSearchQuery : CategoryEvent()
}
