package net.techandgraphics.hymn.ui.screen.searching.category

import net.techandgraphics.hymn.domain.model.Category

data class CategoryState(
  val categories: List<Category> = emptyList(),
  val route: String = "",
  var searchQuery: String = "",
  var isSearching: Boolean = false,
)
