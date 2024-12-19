package net.techandgraphics.hymn.ui.screen.search.category

import net.techandgraphics.hymn.domain.model.Category

data class CategoryUiState(
  val categories: List<Category> = emptyList(),
  val route: String = "",
  var searchQuery: String = "",
  var isSearching: Boolean = false,
)
