package net.techandgraphics.hymn.ui.screen.category

import net.techandgraphics.hymn.domain.model.Category

data class CategoryState(
  val categories: List<Category> = emptyList(),
  val route: String = ""
)
