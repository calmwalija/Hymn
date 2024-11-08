package net.techandgraphics.hymn.ui.screen.searching.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CategoryScreen(
  state: CategoryState,
  event: (CategoryEvent) -> Unit,
) {
  Column {
    CategorySearchBox(state, event)
    LazyColumn {
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
