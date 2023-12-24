package net.techandgraphics.hymn.ui.screen.search

sealed class SearchEvent {
  class OnSearchQuery(val searchQuery: String) : SearchEvent()
  class SearchQueryTag(val searchQuery: String) : SearchEvent()
  object InsertSearchTag : SearchEvent()
  object ClearSearchQuery : SearchEvent()
}
