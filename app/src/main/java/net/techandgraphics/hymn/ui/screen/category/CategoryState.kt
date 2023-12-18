package net.techandgraphics.hymn.ui.screen.category

import net.techandgraphics.hymn.data.local.join.Category

data class CategoryState(
  val categories: List<Category> = emptyList(),
  val route: String = ""
)
