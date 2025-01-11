package net.techandgraphics.hymn.ui.screen.search.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CategoryScreen(
  state: CategoryUiState,
  event: (CategoryUiEvent) -> Unit,
) {
  Column {
    CategorySearchBox(state, event)
    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
      items(state.categories, key = { it.lyric.categoryId }) {
        CategoryScreenItem(
          category = it,
          event = event,
          modifier = Modifier.animateItem()
        )
      }
    }
  }
}
