package net.techandgraphics.hymn.ui.screen.category

sealed class CategoryEvent {
  class Click(val categoryId: Int) : CategoryEvent()
}
