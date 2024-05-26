package net.techandgraphics.hymn.ui.screen.search

import net.techandgraphics.hymn.domain.model.Search

sealed class SearchEvent {
  class OnSearchQuery(val searchQuery: String) : SearchEvent()
  class SearchQueryTag(val searchQuery: String) : SearchEvent()
  data object InsertSearchTag : SearchEvent()
  data object ClearSearchQuery : SearchEvent()
  class OnLongPress(val search: Search) : SearchEvent()
}
